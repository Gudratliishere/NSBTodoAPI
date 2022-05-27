package com.gudratli.nsbtodoapi.service.impl;

import com.gudratli.nsbtodoapi.entity.Status;
import com.gudratli.nsbtodoapi.exception.duplicate.DuplicateStatusException;
import com.gudratli.nsbtodoapi.repository.StatusRepository;
import com.gudratli.nsbtodoapi.service.inter.StatusService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static com.gudratli.nsbtodoapi.util.Entities.getStatus;
import static com.gudratli.nsbtodoapi.util.Entities.getStatusList;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class StatusServiceImplTest
{
    private StatusRepository statusRepository;

    private StatusService statusService;

    @BeforeEach
    public void setUp ()
    {
        statusRepository = mock(StatusRepository.class);

        statusService = new StatusServiceImpl(statusRepository);
    }

    @Test
    public void testGetAll ()
    {
        List<Status> expected = getStatusList();
        when(statusRepository.findAll()).thenReturn(expected);

        List<Status> actual = statusService.getAll();

        assertEquals(expected, actual);
        verify(statusRepository).findAll();
    }

    @Test
    public void testGetById ()
    {
        Status expected = getStatus();
        when(statusRepository.findById(expected.getId())).thenReturn(Optional.of(expected));

        Status actual = statusService.getById(expected.getId());

        assertEquals(expected, actual);
        verify(statusRepository).findById(expected.getId());
    }

    @Test
    public void testGetByName ()
    {
        Status expected = getStatus();
        when(statusRepository.findByName(expected.getName())).thenReturn(expected);

        Status actual = statusService.getByName(expected.getName());

        assertEquals(expected, actual);
        verify(statusRepository).findByName(expected.getName());
    }

    @Test
    public void testAdd_whenValidStatus () throws DuplicateStatusException
    {
        Status status = new Status("complete");
        Status expected = getStatus(status.getName(), 5);
        when(statusRepository.save(status)).thenReturn(expected);
        when(statusRepository.findByName(status.getName())).thenReturn(null);

        Status actual = statusService.add(status);

        assertEquals(expected, actual);
        verify(statusRepository).save(status);
        verify(statusRepository).findByName(status.getName());
    }

    @Test
    public void testAdd_whenDuplicateStatus_itShouldThrowException ()
    {
        Status status = new Status("complete");
        Status expected = getStatus(status.getName(), 5);
        when(statusRepository.findByName(status.getName())).thenReturn(expected);

        try
        {
            statusService.add(status);
        } catch (DuplicateStatusException e)
        {
            assertInstanceOf(DuplicateStatusException.class, e);
        }

        verify(statusRepository).findByName(status.getName());
    }

    @Test
    public void testUpdate_whenValidStatus () throws DuplicateStatusException
    {
        Status status = getStatus("complete", 4);
        Status expected = getStatus(status.getName(), status.getId());
        when(statusRepository.save(status)).thenReturn(expected);
        when(statusRepository.findByName(status.getName())).thenReturn(null);

        Status actual = statusService.update(status);

        assertEquals(expected, actual);
        verify(statusRepository).save(status);
        verify(statusRepository).findByName(status.getName());
    }

    @Test
    public void testUpdate_whenDuplicateStatus_itShouldThrowException ()
    {
        Status status = getStatus("complete", 4);
        Status expected = getStatus(status.getName(), status.getId());
        when(statusRepository.findByName(status.getName())).thenReturn(expected);

        try
        {
            statusService.update(status);
        } catch (DuplicateStatusException e)
        {
            assertInstanceOf(DuplicateStatusException.class, e);
        }

        verify(statusRepository).findByName(status.getName());
    }

    @Test
    public void testRemove ()
    {
        Status status = getStatus();
        when(statusRepository.findById(status.getId())).thenReturn(Optional.of(status));

        statusService.remove(status.getId());

        when(statusRepository.findById(status.getId())).thenReturn(Optional.empty());

        Status actual = statusService.getById(status.getId());

        assertNull(actual);
    }
}