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

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/task")
public class TaskController
{
    private final TaskService taskService;
    private final FileService fileService;
    private final Converter converter;

    @GetMapping(value = {"/getAll", ""})
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public ResponseEntity<ResponseDTO<List<TaskDTO>>> getAll ()
    {
        List<Task> tasks = taskService.getAll();

        return ResponseEntity.ok(getResponseWithList(tasks));
    }

    @GetMapping("/getByNameContaining/{name}")
    @PreAuthorize("hasAnyAuthority('USER', 'ADMIN')")
    public ResponseEntity<ResponseDTO<List<TaskDTO>>> getByNameContaining (@PathVariable String name)
    {
        List<Task> tasks = taskService.getByNameContaining(name);

        return ResponseEntity.ok(getResponseWithList(tasks));
    }

    @GetMapping(value = {"/getById/{id}", "/{id}"})
    @PreAuthorize("hasAnyAuthority('USER', 'ADMIN')")
    public ResponseEntity<ResponseDTO<TaskDTO>> getById (@PathVariable Integer id)
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
    public ResponseEntity<ResponseDTO<TaskDTO>> add (@RequestBody TaskDTO taskDTO)
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
    public ResponseEntity<ResponseDTO<FileDTO>> uploadDocument (@PathVariable Integer id,
            @RequestParam("document") MultipartFile multipartFile)
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
    public ResponseEntity<ResponseDTO<FileDTO>> uploadResult (@PathVariable Integer id,
            @RequestParam("result") MultipartFile multipartFile)
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
    public ResponseEntity<Resource> downloadDocument (@PathVariable Integer id)
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
    public ResponseEntity<Resource> downloadResult (@PathVariable Integer id)
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
    public ResponseEntity<ResponseDTO<TaskDTO>> update (@RequestBody TaskDTO taskDTO)
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
    public ResponseEntity<ResponseDTO<TaskDTO>> delete (@PathVariable Integer id)
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