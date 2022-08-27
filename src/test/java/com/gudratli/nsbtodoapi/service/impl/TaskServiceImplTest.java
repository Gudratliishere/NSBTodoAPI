package com.gudratli.nsbtodoapi.service.impl;

import com.gudratli.nsbtodoapi.entity.Task;
import com.gudratli.nsbtodoapi.repository.TaskRepository;
import com.gudratli.nsbtodoapi.service.inter.TaskService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static com.gudratli.nsbtodoapi.util.Entities.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.*;

class TaskServiceImplTest
{
    private TaskRepository taskRepository;

    private TaskService taskService;

    @BeforeEach
    public void setUp ()
    {
        taskRepository = mock(TaskRepository.class);

        taskService = new TaskServiceImpl(taskRepository);
    }

    @Test
    public void testGetAll ()
    {
        List<Task> expected = getTaskList();
        when(taskRepository.findAll()).thenReturn(expected);

        List<Task> actual = taskService.getAll();

        assertEquals(expected, actual);
        verify(taskRepository).findAll();
    }

    @Test
    public void testGetByNameContaining ()
    {
        List<Task> expected = getTaskList();
        when(taskRepository.findByNameContaining("fill")).thenReturn(expected);

        List<Task> actual = taskService.getByNameContaining("fill");

        assertEquals(expected, actual);
        verify(taskRepository).findByNameContaining("fill");
    }

    @Test
    public void testGetById ()
    {
        Task expected = getTask();
        when(taskRepository.findById(expected.getId())).thenReturn(Optional.of(expected));

        Task actual = taskService.getById(expected.getId());

        assertEquals(expected, actual);
        verify(taskRepository).findById(expected.getId());
    }

    @Test
    public void testAdd ()
    {
        Task task = new Task("Rename project", "You will rename project", getFile(), getFile());
        Task expected = getTask(task.getName(), 5);
        when(taskRepository.save(task)).thenReturn(expected);

        Task actual = taskService.add(task);

        assertEquals(expected, actual);
        verify(taskRepository).save(task);
    }

    @Test
    public void testUpdate ()
    {
        Task task = getTask("Rename project", 5);
        Task expected = getTask(task.getName(), task.getId());
        when(taskRepository.save(task)).thenReturn(expected);

        Task actual = taskService.update(task);

        assertEquals(expected, actual);
        verify(taskRepository).save(task);
    }

    @Test
    public void testRemove ()
    {
        Task task = getTask();
        when(taskRepository.findById(task.getId())).thenReturn(Optional.of(task));

        taskService.remove(task.getId());

        when(taskRepository.findById(task.getId())).thenReturn(Optional.empty());

        Task actual = taskService.getById(task.getId());

        assertNull(actual);
    }
}