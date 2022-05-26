package com.gudratli.nsbtodoapi.service.impl;

import com.gudratli.nsbtodoapi.entity.Country;
import com.gudratli.nsbtodoapi.entity.Region;
import com.gudratli.nsbtodoapi.exception.duplicate.DuplicateCountryException;
import com.gudratli.nsbtodoapi.repository.CountryRepository;
import com.gudratli.nsbtodoapi.repository.RegionRepository;
import com.gudratli.nsbtodoapi.service.inter.CountryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static com.gudratli.nsbtodoapi.util.Entities.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CountryServiceImplTest
{
    private CountryService countryService;

    private CountryRepository countryRepository;
    private RegionRepository regionRepository;

    @BeforeEach
    public void setUp ()
    {
        countryRepository = mock(CountryRepository.class);
        regionRepository = mock(RegionRepository.class);

        countryService = new CountryServiceImpl(countryRepository, regionRepository);
    }

    @Test
    public void testGetAll ()
    {
        List<Country> expected = getCountryList();
        when(countryRepository.findAll()).thenReturn(expected);

        List<Country> actual = countryService.getAll();

        assertEquals(expected, actual);
        verify(countryRepository).findAll();
    }

    @Test
    public void testGetByNameContaining ()
    {
        List<Country> expected = getCountryList();
        when(countryRepository.findByNameContaining("Turk")).thenReturn(expected);

        List<Country> actual = countryService.getByNameContaining("Turk");

        assertEquals(expected, actual);
        verify(countryRepository).findByNameContaining("Turk");
    }

    @Test
    public void testGetByRegionId ()
    {
        List<Country> expected = getCountryList();
        Region region = getRegion();
        when(regionRepository.findById(region.getId())).thenReturn(Optional.of(region));
        when(countryRepository.findByRegion(region)).thenReturn(expected);

        List<Country> actual = countryService.getByRegionId(region.getId());

        assertEquals(expected, actual);
        verify(regionRepository).findById(region.getId());
        verify(countryRepository).findByRegion(region);
    }

    @Test
    public void testGetById ()
    {
        Country expected = getCountry();
        when(countryRepository.findById(expected.getId())).thenReturn(Optional.of(expected));

        Country actual = countryService.getById(expected.getId());

        assertEquals(expected, actual);
        verify(countryRepository).findById(expected.getId());
    }

    @Test
    public void testGetByName ()
    {
        Country expected = getCountry();
        when(countryRepository.findByNameContaining(expected.getName())).thenReturn(
                Collections.singletonList(expected));

        Country actual = countryService.getByName(expected.getName());

        assertEquals(expected, actual);
        verify(countryRepository).findByNameContaining(expected.getName());
    }

    @Test
    public void testAdd_whenValidCountry () throws DuplicateCountryException
    {
        Country country = new Country("india", getRegion());
        Region region = getRegion();
        Country expected = getCountry(country.getName(), 3);
        when(countryRepository.save(country)).thenReturn(expected);
        when(countryRepository.findByNameAndRegion(country.getName(), region)).thenReturn(null);

        Country actual = countryService.add(country);

        assertEquals(expected, actual);
        verify(countryRepository).save(country);
        verify(countryRepository).findByNameAndRegion(country.getName(), region);
    }

    @Test
    public void testAdd_whenDuplicateCountry ()
    {
        Country country = new Country("india", getRegion());
        Country expected = getCountry(country.getName(), 3);
        when(countryRepository.findByNameAndRegion(country.getName(), country.getRegion())).thenReturn(expected);

        try
        {
            countryService.add(country);
        } catch (DuplicateCountryException e)
        {
            assertInstanceOf(DuplicateCountryException.class, e);
        }

        verify(countryRepository).findByNameAndRegion(country.getName(), country.getRegion());
    }

    @Test
    public void testUpdate_whenValidCountry () throws DuplicateCountryException
    {
        Country country = new Country("india", getRegion());
        Region region = getRegion();
        Country expected = getCountry(country.getName(), 3);
        when(countryRepository.save(country)).thenReturn(expected);
        when(countryRepository.findByNameAndRegion(country.getName(), region)).thenReturn(null);

        Country actual = countryService.update(country);

        assertEquals(expected, actual);
        verify(countryRepository).save(country);
        verify(countryRepository).findByNameAndRegion(country.getName(), region);
    }

    @Test
    public void testUpdate_whenDuplicateCountry ()
    {
        Country country = getCountry();
        Country expected = getCountry(country.getName(), country.getId() + 1);
        when(countryRepository.findByNameAndRegion(country.getName(), country.getRegion())).thenReturn(country);

        try
        {
            countryService.update(expected);
        } catch (DuplicateCountryException e)
        {
            assertInstanceOf(DuplicateCountryException.class, e);
        }

        verify(countryRepository).findByNameAndRegion(country.getName(), country.getRegion());
    }

    @Test
    public void testRemove ()
    {
        Country country = getCountry();
        when(countryRepository.findById(country.getId())).thenReturn(Optional.of(country));

        countryService.remove(country.getId());

        when(countryRepository.findById(country.getId())).thenReturn(Optional.empty());
        Country actual = countryService.getById(country.getId());

        assertNull(actual);
    }
}