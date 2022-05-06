package com.gudratli.nsbtodoapi.repository;

import com.gudratli.nsbtodoapi.NsbTodoApiApplication;
import com.gudratli.nsbtodoapi.entity.Region;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.transaction.Transactional;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

@ExtendWith(SpringExtension.class)
@Transactional
@SpringBootTest(classes = NsbTodoApiApplication.class)
class RegionRepositoryTest
{
    @Autowired
    private RegionRepository regionRepository;

    @Test
    public void testFindById_whenValidId ()
    {
        Region region = getRegion();
        Region result = regionRepository.findById(1).orElse(null);

        assertEquals(region, result);
    }

    @Test
    public void testFindById_whenNotValidId ()
    {
        Region result = regionRepository.findById(0).orElse(null);

        assertNull(result);
    }

    @Test
    public void testFindAll ()
    {
        List<Region> regions = getRegionList();
        List<Region> result = regionRepository.findAll();

        assertEquals(regions, result);
    }

    @Test
    public void testFindByNameContaining_whenValidFullName ()
    {
        List<Region> regions = Collections.singletonList(getRegion());
        List<Region> result = regionRepository.findByNameContaining("Asia");

        assertEquals(regions, result);
    }

    @Test
    public void testFindByNameContaining_whenValidPreName ()
    {
        List<Region> regions = Collections.singletonList(getRegion());
        List<Region> result = regionRepository.findByNameContaining("As");

        assertEquals(regions, result);
    }

    @Test
    public void testFindByNameContaining_whenValidSufName ()
    {
        List<Region> regions = Collections.singletonList(getRegion());
        List<Region> result = regionRepository.findByNameContaining("ia");

        assertEquals(regions, result);
    }

    @Test
    public void testFindByNameContaining_whenValidMiddleName ()
    {
        List<Region> regions = Collections.singletonList(getRegion());
        List<Region> result = regionRepository.findByNameContaining("si");

        assertEquals(regions, result);
    }

    @Test
    public void testFindByNameContaining_whenEmptyName ()
    {
        List<Region> expected = regionRepository.findAll();
        List<Region> actual = regionRepository.findByNameContaining("");

        assertEquals(expected, actual);
    }

    @Test
    public void testAddRegion ()
    {
        Region region = new Region("America");
        Region expected = getRegion("America", 9);
        Region actual = regionRepository.save(region);

        assertEquals(expected, actual);
    }

    @Test
    public void testUpdateRegion ()
    {
        Region region = regionRepository.getById(2);
        region.setName("EuropaUpdated");
        region = regionRepository.save(region);

        Region expected = getRegion("EuropaUpdated", 2);

        assertEquals(expected, region);
    }

    @Test
    public void testDeleteRegion ()
    {
        regionRepository.findById(3).ifPresent(region -> regionRepository.delete(region));
        Region actual = regionRepository.findById(3).orElse(null);

        assertNull(actual);
    }

    private Region getRegion ()
    {
        Region region = new Region("Asia");
        region.setId(1);
        return region;
    }

    private Region getRegion (String name, int id)
    {
        Region region = new Region(name);
        region.setId(id);
        return region;
    }

    private List<Region> getRegionList ()
    {
        return Arrays.asList(getRegion("Asia", 1), getRegion("Europa", 2));
    }
}