package com.gudratli.nsbtodoapi.service.impl;

import com.gudratli.nsbtodoapi.entity.Technology;
import com.gudratli.nsbtodoapi.exception.duplicate.DuplicateTechnologyException;
import com.gudratli.nsbtodoapi.repository.TechnologyRepository;
import com.gudratli.nsbtodoapi.service.inter.TechnologyService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static com.gudratli.nsbtodoapi.util.Entities.getTechnology;
import static com.gudratli.nsbtodoapi.util.Entities.getTechnologyList;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TechnologyServiceImplTest
{
    private TechnologyRepository technologyRepository;

    private TechnologyService technologyService;

    @BeforeEach
    public void setUp ()
    {
        technologyRepository = mock(TechnologyRepository.class);

        technologyService = new TechnologyServiceImpl(technologyRepository);
    }

    @Test
    public void testGetAll ()
    {
        List<Technology> expected = getTechnologyList();
        when(technologyRepository.findAll()).thenReturn(expected);

        List<Technology> actual = technologyService.getAll();

        assertEquals(expected, actual);
        verify(technologyRepository).findAll();
    }

    @Test
    public void testGetByNameContaining ()
    {
        List<Technology> expected = getTechnologyList();
        when(technologyRepository.findByNameContaining("html")).thenReturn(expected);

        List<Technology> actual = technologyService.getByNameContaining("html");

        assertEquals(expected, actual);
        verify(technologyRepository).findByNameContaining("html");
    }

    @Test
    public void testGetById ()
    {
        Technology expected = getTechnology();
        when(technologyRepository.findById(expected.getId())).thenReturn(Optional.of(expected));

        Technology actual = technologyService.getById(expected.getId());

        assertEquals(expected, actual);
        verify(technologyRepository).findById(expected.getId());
    }

    @Test
    public void testGetByName ()
    {
        Technology expected = getTechnology();
        when(technologyRepository.findByNameContaining(expected.getName())).thenReturn(
                Collections.singletonList(expected));

        Technology actual = technologyService.getByName(expected.getName());

        assertEquals(expected, actual);
        verify(technologyRepository).findByNameContaining(expected.getName());
    }

    @Test
    public void testAdd_whenValidTechnology () throws DuplicateTechnologyException
    {
        Technology technology = new Technology("htm");
        Technology expected = getTechnology(technology.getName(), 4);
        when(technologyRepository.save(technology)).thenReturn(expected);
        when(technologyRepository.findByNameContaining(technology.getName())).thenReturn(Collections.emptyList());

        Technology actual = technologyService.add(technology);

        assertEquals(expected, actual);
        verify(technologyRepository).save(technology);
        verify(technologyRepository).findByNameContaining(technology.getName());
    }

    @Test
    public void testAdd_whenDuplicateTechnology ()
    {
        Technology technology = new Technology("htm");
        Technology expected = getTechnology(technology.getName(), 4);
        when(technologyRepository.findByNameContaining(technology.getName())).thenReturn(
                Collections.singletonList(expected));

        try
        {
            technologyService.add(technology);
        } catch (DuplicateTechnologyException e)
        {
            assertInstanceOf(DuplicateTechnologyException.class, e);
        }

        verify(technologyRepository).findByNameContaining(technology.getName());
    }

    @Test
    public void testUpdate_whenValidTechnology () throws DuplicateTechnologyException
    {
        Technology technology = getTechnology("htm", 3);
        Technology expected = getTechnology(technology.getName(), technology.getId() + 1);
        when(technologyRepository.save(technology)).thenReturn(expected);
        when(technologyRepository.findByNameContaining(technology.getName())).thenReturn(Collections.emptyList());

        Technology actual = technologyService.update(technology);

        assertEquals(expected, actual);
        verify(technologyRepository).save(technology);
        verify(technologyRepository).findByNameContaining(technology.getName());
    }

    @Test
    public void testUpdate_whenDuplicateTechnology ()
    {
        Technology technology = getTechnology("htm", 3);
        Technology expected = getTechnology(technology.getName(), technology.getId() + 1);
        when(technologyRepository.findByNameContaining(technology.getName())).thenReturn(
                Collections.singletonList(expected));

        try
        {
            technologyService.update(technology);
        } catch (DuplicateTechnologyException e)
        {
            assertInstanceOf(DuplicateTechnologyException.class, e);
        }

        verify(technologyRepository).findByNameContaining(technology.getName());
    }

    @Test
    public void testRemove ()
    {
        Technology technology = getTechnology();
        when(technologyRepository.findById(technology.getId())).thenReturn(Optional.of(technology));

        technologyService.remove(technology.getId());

        when(technologyRepository.findById(technology.getId())).thenReturn(Optional.empty());

        Technology actual = technologyService.getById(technology.getId());

        assertNull(actual);
    }
}