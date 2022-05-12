package com.gudratli.nsbtodoapi.repository;

import com.gudratli.nsbtodoapi.entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TaskRepository extends JpaRepository<Task, Integer>
{
    List<Task> findByNameContaining (String name);
}