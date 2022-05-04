package com.gudratli.nsbtodoapi.repository;

import com.gudratli.nsbtodoapi.entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskRepository extends JpaRepository<Task, Integer>
{
}