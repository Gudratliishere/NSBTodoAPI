package com.gudratli.nsbtodoapi.controller;

import com.gudratli.nsbtodoapi.dto.Converter;
import com.gudratli.nsbtodoapi.dto.FileDTO;
import com.gudratli.nsbtodoapi.dto.ResponseDTO;
import com.gudratli.nsbtodoapi.dto.TaskDTO;
import com.gudratli.nsbtodoapi.entity.File;
import com.gudratli.nsbtodoapi.entity.Task;
import com.gudratli.nsbtodoapi.service.inter.FileService;
import com.gudratli.nsbtodoapi.service.inter.TaskService;
import com.gudratli.nsbtodoapi.util.FileUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/task")
@Api("Task controller")
public class TaskController
{
    private final TaskService taskService;
    private final FileService fileService;
    private final Converter converter;

    @GetMapping(value = {"/getAll", ""})
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    @ApiOperation(value = "Get All", notes = "Get all tasks.")
    public ResponseEntity<ResponseDTO<List<TaskDTO>>> getAll ()
    {
        List<Task> tasks = taskService.getAll();

        return ResponseEntity.ok(getResponseWithList(tasks));
    }

    @GetMapping("/getByNameContaining/{name}")
    @PreAuthorize("hasAnyAuthority('USER', 'ADMIN')")
    @ApiOperation(value = "Get by name containing", notes = "Get list of tasks that contains that key.")
    public ResponseEntity<ResponseDTO<List<TaskDTO>>> getByNameContaining (@PathVariable @ApiParam(name = "Name",
            value = "Name of the task", required = true, example = "If else") String name)
    {
        List<Task> tasks = taskService.getByNameContaining(name);

        return ResponseEntity.ok(getResponseWithList(tasks));
    }

    @GetMapping(value = {"/getById/{id}", "/{id}"})
    @PreAuthorize("hasAnyAuthority('USER', 'ADMIN')")
    @ApiOperation(value = "Get by ID", notes = "Get single task according to ID.")
    public ResponseEntity<ResponseDTO<TaskDTO>> getById (@PathVariable @ApiParam(name = "ID",
            value = "ID of the task", required = true, example = "21") Integer id)
    {
        Task task = taskService.getById(id);
        ResponseDTO<TaskDTO> responseDTO = new ResponseDTO<>();

        if (task == null)
            responseDTO.notFound("task", "id.");
        else
            responseDTO.successfullyFetched(converter.toTaskDTO(task));

        return ResponseEntity.ok(responseDTO);
    }

    @PostMapping
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    @ApiOperation(value = "Add", notes = "Add new task.")
    public ResponseEntity<ResponseDTO<TaskDTO>> add (@Valid @RequestBody @ApiParam(name = "Task",
            value = "DTO for task", required = true) TaskDTO taskDTO)
    {
        Task task = converter.toTask(taskDTO);
        task.setId(null);

        task = taskService.add(task);

        ResponseDTO<TaskDTO> responseDTO = new ResponseDTO<>();
        responseDTO.successfullyInserted(converter.toTaskDTO(task));

        return ResponseEntity.ok(responseDTO);
    }

    @PostMapping("/uploadDocument/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    @ApiOperation(value = "Upload document", notes = "Upload document about task.")
    public ResponseEntity<ResponseDTO<FileDTO>> uploadDocument (@PathVariable @ApiParam(name = "ID",
            value = "ID of the task.", required = true, example = "22") Integer id,
            @RequestParam("document") @ApiParam(name = "Document", value = "Document of task", required = true)
                    MultipartFile multipartFile)
    {
        Task task = taskService.getById(id);
        ResponseDTO<FileDTO> responseDTO = new ResponseDTO<>();

        if (task == null)
            return ResponseEntity.ok(responseDTO.notFound("task", "id."));

        if (multipartFile != null && multipartFile.getOriginalFilename() != null)
        {
            String fileName = "Document" + id + '.' + StringUtils.getFilenameExtension(
                    multipartFile.getOriginalFilename());

            File file = new File(fileName, multipartFile.getContentType(), multipartFile.getSize());
            FileDTO fileDTO = converter.toFileDTO(file);
            fileDTO.setDownloadURL("http://localhost:8080/task/downloadDocument/" + id);

            file = fileService.add(file);
            task.setDocumentation(file);
            taskService.update(task);

            responseDTO.successfullyUpdated(fileDTO);

            try
            {
                FileUtil.saveFile(FileUtil.ATTACHMENT_FILE_DIRECTORY, fileName, multipartFile);
            } catch (IOException e)
            {
                responseDTO.setErrorCode(500);
                responseDTO.setErrorMessage(e.getMessage());
            }
        }

        return ResponseEntity.ok(responseDTO);
    }

    @PostMapping("/uploadResult/{id}")
    @PreAuthorize("hasAnyAuthority('USER', 'ADMIN')")
    @ApiOperation(value = "Upload result", notes = "Upload result of task.")
    public ResponseEntity<ResponseDTO<FileDTO>> uploadResult (@PathVariable @ApiParam(name = "ID",
            value = "ID of the task.", required = true, example = "22") Integer id,
            @RequestParam("result") @ApiParam(name = "Result", value = "Result of task", required = true)
                    MultipartFile multipartFile)
    {
        Task task = taskService.getById(id);
        ResponseDTO<FileDTO> responseDTO = new ResponseDTO<>();

        if (task == null)
            return ResponseEntity.ok(responseDTO.notFound("task", "id."));

        if (multipartFile != null && multipartFile.getOriginalFilename() != null)
        {
            String fileName = "Result" + id + '.' + StringUtils.getFilenameExtension(
                    multipartFile.getOriginalFilename());

            File file = new File(fileName, multipartFile.getContentType(), multipartFile.getSize());
            FileDTO fileDTO = converter.toFileDTO(file);
            fileDTO.setDownloadURL("http://localhost:8080/task/downloadResult/" + id);

            file = fileService.add(file);
            task.setResult(file);
            taskService.update(task);

            responseDTO.successfullyUpdated(fileDTO);

            try
            {
                FileUtil.saveFile(FileUtil.RESULT_FILE_DIRECTORY, fileName, multipartFile);
            } catch (IOException e)
            {
                responseDTO.setErrorCode(500);
                responseDTO.setErrorMessage(e.getMessage());
            }
        }

        return ResponseEntity.ok(responseDTO);
    }

    @GetMapping("/downloadDocument/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    @ApiOperation(value = "Download document", notes = "Download document about task.")
    public ResponseEntity<Resource> downloadDocument (@PathVariable @ApiParam(name = "ID",
            value = "ID of the task", required = true, example = "26") Integer id)
    {
        Task task = taskService.getById(id);
        if (task == null)
            return ResponseEntity.ok(null);

        java.io.File file = new java.io.File(FileUtil.ATTACHMENT_FILE_DIRECTORY + task.getDocumentation().getName());

        try
        {
            FileInputStream fileInputStream = new FileInputStream(file);
            byte[] arr = new byte[(int) file.length()];
            fileInputStream.read(arr);
            fileInputStream.close();

            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(task.getDocumentation().getType()))
                    .header(HttpHeaders.CONTENT_DISPOSITION,
                            "attachment; filename=\"" + task.getDocumentation().getName() + "\"")
                    .body(new ByteArrayResource(arr));
        } catch (IOException e)
        {
            return ResponseEntity.ok(null);
        }
    }

    @GetMapping("/downloadResult/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    @ApiOperation(value = "Download result", notes = "Download result of task.")
    public ResponseEntity<Resource> downloadResult (@PathVariable @ApiParam(name = "ID",
            value = "ID of the task", required = true, example = "28") Integer id)
    {
        Task task = taskService.getById(id);
        if (task == null)
            return ResponseEntity.ok(null);

        java.io.File file = new java.io.File(FileUtil.RESULT_FILE_DIRECTORY + task.getResult().getName());

        try
        {
            FileInputStream fileInputStream = new FileInputStream(file);
            byte[] arr = new byte[(int) file.length()];
            fileInputStream.read(arr);
            fileInputStream.close();

            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(task.getResult().getType()))
                    .header(HttpHeaders.CONTENT_DISPOSITION,
                            "attachment; filename=\"" + task.getResult().getName() + "\"")
                    .body(new ByteArrayResource(arr));
        } catch (IOException e)
        {
            return ResponseEntity.ok(null);
        }
    }

    @PutMapping
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    @ApiOperation(value = "Update", notes = "Update existing task.")
    public ResponseEntity<ResponseDTO<TaskDTO>> update (@Valid @RequestBody @ApiParam(name = "Task",
            value = "DTO for task", required = true) TaskDTO taskDTO)
    {
        Task task = taskService.getById(taskDTO.getId());
        ResponseDTO<TaskDTO> responseDTO = new ResponseDTO<>();

        if (task == null)
            responseDTO.notFound("task", "id.");
        else
        {
            converter.toTask(task, taskDTO);
            task = taskService.update(task);
            responseDTO.successfullyUpdated(converter.toTaskDTO(task));
        }

        return ResponseEntity.ok(responseDTO);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    @ApiOperation(value = "Delete", notes = "Delete single task with it's ID.")
    public ResponseEntity<ResponseDTO<TaskDTO>> delete (@PathVariable @ApiParam(name = "ID",
            value = "ID of the task", required = true, example = "26") Integer id)
    {
        Task task = taskService.getById(id);
        ResponseDTO<TaskDTO> responseDTO = new ResponseDTO<>();

        if (task == null)
            responseDTO.notFound("task", "id.");
        else
        {
            taskService.remove(id);
            responseDTO.successfullyDeleted(converter.toTaskDTO(task));
        }

        return ResponseEntity.ok(responseDTO);
    }

    private ResponseDTO<List<TaskDTO>> getResponseWithList (List<Task> tasks)
    {
        List<TaskDTO> taskDTOs = new ArrayList<>();

        tasks.forEach(task -> taskDTOs.add(converter.toTaskDTO(task)));

        ResponseDTO<List<TaskDTO>> responseDTO = new ResponseDTO<>();
        responseDTO.successfullyFetched(taskDTOs);

        return responseDTO;
    }
}