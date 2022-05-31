package com.gudratli.nsbtodoapi.service.impl;

import com.gudratli.nsbtodoapi.entity.Process;
import com.gudratli.nsbtodoapi.entity.Task;
import com.gudratli.nsbtodoapi.entity.User;
import com.gudratli.nsbtodoapi.exception.duplicate.DuplicateProcessException;
import com.gudratli.nsbtodoapi.repository.ProcessRepository;
import com.gudratli.nsbtodoapi.repository.TaskRepository;
import com.gudratli.nsbtodoapi.repository.UserRepository;
import com.gudratli.nsbtodoapi.service.inter.ProcessService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static com.gudratli.nsbtodoapi.util.Entities.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ProcessServiceImplTest
{
    private ProcessRepository processRepository;
    private UserRepository userRepository;
    private TaskRepository taskRepository;

    private ProcessService processService;

    @BeforeEach
    public void setUp ()
    {
        processRepository = mock(ProcessRepository.class);
        userRepository = mock(UserRepository.class);
        taskRepository = mock(TaskRepository.class);

        processService = new ProcessServiceImpl(processRepository, userRepository, taskRepository);
    }

    @Test
    public void testGetAll ()
    {
        List<Process> expected = getProcessList();
        when(processRepository.findAll()).thenReturn(expected);

        List<Process> actual = processService.getAll();

        assertEquals(expected, actual);
        verify(processRepository).findAll();
    }

    @Test
    public void testGetByUserId ()
    {
        List<Process> expected = getProcessList();
        User user = getUser();
        when(processRepository.findByUser(user)).thenReturn(expected);
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));

        List<Process> actual = processService.getByUserId(user.getId());

        assertEquals(expected, actual);
        verify(processRepository).findByUser(user);
    }

    @Test
    public void testGetByTaskId ()
    {
        List<Process> expected = getProcessList();
        Task task = getTask();
        when(processRepository.findByTask(task)).thenReturn(expected);
        when(taskRepository.findById(task.getId())).thenReturn(Optional.of(task));

        List<Process> actual = processService.getByTaskId(task.getId());

        assertEquals(expected, actual);
        verify(processRepository).findByTask(task);
    }

    @Test
    public void testGetById ()
    {
        Process expected = getProcess();
        when(processRepository.findById(expected.getId())).thenReturn(Optional.of(expected));

        Process actual = processService.getById(expected.getId());

        assertEquals(expected, actual);
        verify(processRepository).findById(expected.getId());
    }

    @Test
    public void testAdd_whenValidProcess () throws DuplicateProcessException
    {
        Process process = new Process(getUser(), getTask(), parse("2022-03-15 00:05:21"),
                parse("2022-05-12 12:08:23"), parse("2022-08-15 00:05:21"), getStatus());
        Process expected = getProcess("2", 5);
        when(processRepository.save(process)).thenReturn((expected));
        when(processRepository.findByUserAndTask(process.getUser(), process.getTask())).thenReturn(null);

        Process actual = processService.add(process);

        assertEquals(expected, actual);
        verify(processRepository).save(process);
        verify(processRepository).findByUserAndTask(process.getUser(), process.getTask());
    }

    @Test
    public void testAdd_whenDuplicateProcess_itShouldThrowException ()
    {
        Process process = new Process(getUser(), getTask(), parse("2022-03-15 00:05:21"),
                parse("2022-05-12 12:08:23"), parse("2022-08-15 00:05:21"), getStatus());
        Process expected = getProcess("2", 5);
        when(processRepository.findByUserAndTask(process.getUser(), process.getTask())).thenReturn(expected);

        try
        {
            processService.add(process);
        } catch (DuplicateProcessException e)
        {
            assertInstanceOf(DuplicateProcessException.class, e);
        }

        verify(processRepository).findByUserAndTask(process.getUser(), process.getTask());
    }

    @Test
    public void testUpdate_whenValidProcess () throws DuplicateProcessException
    {
        Process process = getProcess("2", 4);
        Process expected = getProcess("2", process.getId() + 1);
        when(processRepository.save(process)).thenReturn((expected));
        when(processRepository.findByUserAndTask(process.getUser(), process.getTask())).thenReturn(null);

        Process actual = processService.update(process);

        assertEquals(expected, actual);
        verify(processRepository).save(process);
        verify(processRepository).findByUserAndTask(process.getUser(), process.getTask());
    }

    @Test
    public void testUpdate_whenDuplicateProcess_itShouldThrowException ()
    {
        Process process = getProcess("2", 3);
        Process expected = getProcess("2", process.getId() + 1);
        when(processRepository.findByUserAndTask(process.getUser(), process.getTask())).thenReturn(expected);

        try
        {
            processService.update(process);
        } catch (DuplicateProcessException e)
        {
            assertInstanceOf(DuplicateProcessException.class, e);
        }

        verify(processRepository).findByUserAndTask(process.getUser(), process.getTask());
    }

    @Test
    public void testRemove ()
    {
        Process process = getProcess();
        when(processRepository.findById(process.getId())).thenReturn(Optional.of(process));

        processService.remove(process.getId());

        when(processRepository.findById(process.getId())).thenReturn(Optional.empty());

        Process actual = processService.getById(process.getId());

        assertNull(actual);
    }
}