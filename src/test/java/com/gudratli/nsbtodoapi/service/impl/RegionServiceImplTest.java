package com.gudratli.nsbtodoapi.service.impl;

import com.gudratli.nsbtodoapi.entity.Region;
import com.gudratli.nsbtodoapi.exception.duplicate.DuplicateRegionException;
import com.gudratli.nsbtodoapi.repository.RegionRepository;
import com.gudratli.nsbtodoapi.service.inter.RegionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static com.gudratli.nsbtodoapi.util.Entities.getRegion;
import static com.gudratli.nsbtodoapi.util.Entities.getRegionList;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class RegionServiceImplTest
{
    private RegionRepository regionRepository;

    private RegionService regionService;

    @BeforeEach
    public void setUp ()
    {
        regionRepository = mock(RegionRepository.class);

        regionService = new RegionServiceImpl(regionRepository);
    }

    @Test
    public void testGetAll ()
    {
        List<Region> expected = getRegionList();
        when(regionRepository.findAll()).thenReturn(expected);

        List<Region> actual = regionService.getAll();

        assertEquals(expected, actual);
        verify(regionRepository).findAll();
    }

    @Test
    public void testGetByNameContaining ()
    {
        List<Region> expected = getRegionList();
        when(regionRepository.findByNameContaining("a")).thenReturn(expected);

        List<Region> actual = regionService.getByNameContaining("a");

        assertEquals(expected, actual);
        verify(regionRepository).findByNameContaining("a");
    }

    @Test
    public void testGetById ()
    {
        Region expected = getRegion();
        when(regionRepository.findById(1)).thenReturn(Optional.of(expected));

        Region actual = regionService.getById(1);

        assertEquals(expected, actual);
        verify(regionRepository).findById(1);
    }

    @Test
    public void testGetByName ()
    {
        Region expected = getRegion();
        when(regionRepository.findByName("Asia")).thenReturn(Collections.singletonList(expected));

        Region actual = regionService.getByName("Asia");

        assertEquals(expected, actual);
        verify(regionRepository).findByName("Asia");
    }

    @Test
    public void testAdd_whenValidRegion () throws DuplicateRegionException
    {
        Region region = new Region("America");
        Region expected = getRegion("America", 3);
        when(regionRepository.save(region)).thenReturn(expected);
        when(regionRepository.findByName("America")).thenReturn(Collections.emptyList());

        Region actual = regionService.add(region);

        assertEquals(expected, actual);
        verify(regionRepository).save(region);
        verify(regionRepository).findByName("America");
    }

    @Test
    public void testAdd_whenDuplicateRegion ()
    {
        Region region = new Region("America");
        Region expected = getRegion("America", 3);
        when(regionRepository.findByName("America")).thenReturn(Collections.singletonList(expected));

        try
        {
            regionService.add(region);
        } catch (DuplicateRegionException e)
        {
            assertInstanceOf(DuplicateRegionException.class, e);
        }

        verify(regionRepository).findByName("America");
    }

    @Test
    public void testUpdate_whenValidRegion () throws DuplicateRegionException
    {
        Region expected = getRegion("AmericaUpdated", 3);
        when(regionRepository.save(expected)).thenReturn(expected);
        when(regionRepository.findByName("AmericaUpdated")).thenReturn(Collections.emptyList());

        Region actual = regionService.update(expected);

        assertEquals(expected, actual);
        verify(regionRepository).save(expected);
        verify(regionRepository).findByName("AmericaUpdated");
    }

    @Test
    public void testUpdate_whenDuplicateRegion ()
    {
        Region region = getRegion("AmericaUpdated", 3);
        Region expected = getRegion("AmericaUpdated", 4);

        when(regionRepository.findByName("AmericaUpdated")).thenReturn(Collections.singletonList(expected));

        try
        {
            regionService.update(region);
        } catch (DuplicateRegionException e)
        {
            assertInstanceOf(DuplicateRegionException.class, e);
        }

        verify(regionRepository).findByName("AmericaUpdated");

    }

    @Test
    public void testRemove ()
    {
        Region region = getRegion();
        when(regionRepository.findById(1)).thenReturn(Optional.of(region));

        regionService.remove(1);

        when(regionRepository.findById(1)).thenReturn(Optional.empty());
        Region actual = regionService.getById(1);

        assertNull(actual);
    }
}