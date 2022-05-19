package com.gudratli.nsbtodoapi.repository;

import com.gudratli.nsbtodoapi.entity.Process;
import com.gudratli.nsbtodoapi.entity.Task;
import com.gudratli.nsbtodoapi.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProcessRepository extends JpaRepository<Process, Integer>
{
    List<Process> findByUser (User user);

    List<Process> findByTask (Task task);

    Process findByUserAndTask (User user, Task task);
}