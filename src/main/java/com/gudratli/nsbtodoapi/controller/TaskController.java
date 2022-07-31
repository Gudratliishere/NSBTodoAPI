package com.gudratli.nsbtodoapi.controller;

import com.gudratli.nsbtodoapi.dto.Converter;
import com.gudratli.nsbtodoapi.dto.ResponseDTO;
import com.gudratli.nsbtodoapi.dto.TaskDTO;
import com.gudratli.nsbtodoapi.entity.Task;
import com.gudratli.nsbtodoapi.service.inter.TaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/task")
public class TaskController
{
    private final TaskService taskService;
    private final Converter converter;

    @GetMapping(value = {"/getAll", ""})
    public ResponseEntity<ResponseDTO<List<TaskDTO>>> getAll ()
    {
        List<Task> tasks = taskService.getAll();

        return ResponseEntity.ok(getResponseWithList(tasks));
    }

    @GetMapping("/getByNameContaining/{name}")
    public ResponseEntity<ResponseDTO<List<TaskDTO>>> getByNameContaining (@PathVariable String name)
    {
        List<Task> tasks = taskService.getByNameContaining(name);

        return ResponseEntity.ok(getResponseWithList(tasks));
    }

    @GetMapping(value = {"/getById/{id}", "/{id}"})
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
    public ResponseEntity<ResponseDTO<TaskDTO>> add (@RequestBody TaskDTO taskDTO)
    {
        Task task = converter.toTask(taskDTO);
        task.setId(null);

        task = taskService.add(task);

        ResponseDTO<TaskDTO> responseDTO = new ResponseDTO<>();
        responseDTO.successfullyInserted(converter.toTaskDTO(task));

        return ResponseEntity.ok(responseDTO);
    }

    @PutMapping
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