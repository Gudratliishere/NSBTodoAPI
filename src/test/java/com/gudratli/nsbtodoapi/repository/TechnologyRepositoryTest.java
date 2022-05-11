package com.gudratli.nsbtodoapi.repository;

import com.gudratli.nsbtodoapi.NsbTodoApiApplication;
import com.gudratli.nsbtodoapi.entity.Technology;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.transaction.Transactional;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@Transactional
@SpringBootTest(classes = NsbTodoApiApplication.class)
class TechnologyRepositoryTest
{
    @Autowired
    private TechnologyRepository technologyRepository;

    @Test
    public void testFindById ()
    {
        Technology expected = getTechnology();
        Technology actual = technologyRepository.findById(1).orElse(null);

        assertEquals(expected, actual);
    }

    @Test
    public void testFindByNameContaining ()
    {
        Technology expected = getTechnology();
        Technology actual = technologyRepository.findByNameContaining("TM").get(0);

        assertEquals(expected, actual);
    }

    @Test
    public void testFindAll ()
    {
        List<Technology> expected = getTechnologies();
        List<Technology> actual = technologyRepository.findAll();

        assertEquals(expected, actual);
    }

    @Test
    public void testAddTechnology ()
    {
        Technology technology = new Technology("JS");
        technology = technologyRepository.save(technology);

        Technology actual = technologyRepository.findById(technology.getId()).orElse(null);
        Technology expected = getTechnology("JS", 3);

        assertEquals(expected, actual);
    }

    @Test
    public void testUpdateTechnology ()
    {
        Technology technology = technologyRepository.findById(2).orElse(null);
        if (technology !=null)
        {
            technology.setName("CSSUpdated");
            technologyRepository.save(technology);
        }

        Technology actual = technologyRepository.findById(2).orElse(null);
        Technology expected = getTechnology("CSSUpdated", 2);

        assertEquals(expected, actual);
    }

    @Test
    public void testDeleteTechnology ()
    {
        technologyRepository.findById(2).ifPresent(technology -> technologyRepository.delete(technology));

        Technology actual = technologyRepository.findById(2).orElse(null);

        assertNull(actual);
    }

    private Technology getTechnology ()
    {
        Technology technology = new Technology("HTML");
        technology.setId(1);
        return technology;
    }

    private Technology getTechnology (String name, int id)
    {
        Technology technology = new Technology(name);
        technology.setId(id);
        return technology;
    }

    private List<Technology> getTechnologies ()
    {
        return Arrays.asList(getTechnology(), getTechnology("CSS", 2));
    }
}