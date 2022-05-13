package com.gudratli.nsbtodoapi.service.inter;

import com.gudratli.nsbtodoapi.entity.Task;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface TaskService
{
    List<Task> getAll ();

    List<Task> getByNameContaining (String name);

    Task getById (Integer id);

    Task add (Task task);

    Task update (Task task);

    void remove (Integer id);
}
