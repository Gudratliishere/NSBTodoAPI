package com.gudratli.nsbtodoapi.service.impl;

import com.gudratli.nsbtodoapi.repository.ProcessRepository;
import com.gudratli.nsbtodoapi.repository.TaskRepository;
import com.gudratli.nsbtodoapi.repository.UserRepository;
import com.gudratli.nsbtodoapi.service.inter.ProcessService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

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
    }

    @Test
    public void testGetByUserId ()
    {
    }

    @Test
    public void testGetByTaskId ()
    {
    }

    @Test
    public void testGetById ()
    {
    }

    @Test
    public void testAdd ()
    {
    }

    @Test
    public void testUpdate ()
    {
    }

    @Test
    public void testRemove ()
    {
    }
}