package com.gudratli.nsbtodoapi.service.impl;

import com.gudratli.nsbtodoapi.entity.Task;
import com.gudratli.nsbtodoapi.repository.TaskRepository;
import com.gudratli.nsbtodoapi.service.inter.TaskService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TaskServiceImpl implements TaskService
{
    private final TaskRepository taskRepository;

    public TaskServiceImpl (TaskRepository taskRepository) {this.taskRepository = taskRepository;}

    @Override
    public List<Task> getAll ()
    {
        return taskRepository.findAll();
    }

    @Override
    public List<Task> getByNameContaining (String name)
    {
        return taskRepository.findByNameContaining(name);
    }

    @Override
    public Task getById (Integer id)
    {
        return taskRepository.findById(id).orElse(null);
    }

    @Override
    public Task add (Task task)
    {
        return taskRepository.save(task);
    }

    @Override
    public Task update (Task task)
    {
        return taskRepository.save(task);
    }

    @Override
    public void remove (Integer id)
    {
        taskRepository.findById(id).ifPresent(taskRepository::delete);
    }
}
