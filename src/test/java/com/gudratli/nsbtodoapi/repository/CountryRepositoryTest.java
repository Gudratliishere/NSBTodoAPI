package com.gudratli.nsbtodoapi.repository;

import com.gudratli.nsbtodoapi.NsbTodoApiApplication;
import com.gudratli.nsbtodoapi.entity.Country;
import com.gudratli.nsbtodoapi.entity.Region;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.transaction.Transactional;
import java.util.List;

import static com.gudratli.nsbtodoapi.util.Entities.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

@ExtendWith(SpringExtension.class)
@Transactional
@SpringBootTest(classes = NsbTodoApiApplication.class)
class CountryRepositoryTest
{
    @Autowired
    private CountryRepository countryRepository;

    @Autowired
    private RegionRepository regionRepository;

    @Test
    public void testFindById ()
    {
        Country expected = getCountry("UK");
        Country actual = countryRepository.findById(1).orElse(null);

        assertEquals(expected, actual);
    }

    @Test
    public void testFindAll ()
    {
        List<Country> expected = getCountryList();
        List<Country> actual = countryRepository.findAll();

        assertEquals(expected, actual);
    }

    @Test
    public void testFindByNameContaining ()
    {
        Country expected = getCountry("Russia", 2);
        Country actual = countryRepository.findByNameContaining("uss").get(0);

        assertEquals(expected, actual);
    }

    @Test
    public void testAddCountry ()
    {
        Country country = getCountry("Turkey");
        Country actual = countryRepository.save(country);
        Country expected = getCountry("Turkey", 1);

        assertEquals(expected, actual);
    }

    @Test
    public void testUpdateCountry ()
    {
        Country expected = getCountry("RussiaUpdated", 2);
        Country actual = countryRepository.findById(2).orElse(null);
        if (actual != null)
        {
            actual.setName("RussiaUpdated");
            actual = countryRepository.save(actual);
        }

        assertEquals(expected, actual);
    }

    @Test
    public void testUpdateCountry_whenRegionNull ()
    {
        Country expected = getCountry("RussiaUpdated", 2);
        Country actual = countryRepository.findById(2).orElse(null);
        if (actual != null)
        {
            actual.setName("RussiaUpdated");
            actual.setRegion(null);
            actual = countryRepository.save(actual);
        }

        assertEquals(expected, actual);
    }

    @Test
    public void testDeleteCountry ()
    {
        countryRepository.findById(2).ifPresent(country -> countryRepository.delete(country));
        Country country = countryRepository.findById(2).orElse(null);

        assertNull(country);
    }
}