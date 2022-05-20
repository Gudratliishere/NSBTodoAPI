package com.gudratli.nsbtodoapi.repository;

import com.gudratli.nsbtodoapi.NsbTodoApiApplication;
import com.gudratli.nsbtodoapi.entity.UserTechnology;
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
class UserTechnologyRepositoryTest
{
    @Autowired
    private UserTechnologyRepository userTechnologyRepository;

    @Test
    public void testFindById ()
    {
        UserTechnology expected = getUserTechnology();
        UserTechnology actual = userTechnologyRepository.findById(1).orElse(null);

        assertEquals(expected, actual);
    }

    @Test
    public void testFindByUser ()
    {
        List<UserTechnology> expected = getUserTechnologyList();
        List<UserTechnology> actual = userTechnologyRepository.findByUser(getUser());

        assertEquals(expected, actual);
    }

    @Test
    public void testFindByTechnology ()
    {
        List<UserTechnology> expected = getUserTechnologyList();
        List<UserTechnology> actual = userTechnologyRepository.findByTechnology(getTechnology());

        assertEquals(expected, actual);
    }

    @Test
    public void testFindAll ()
    {
        List<UserTechnology> expected = getUserTechnologyList();
        List<UserTechnology> actual = userTechnologyRepository.findAll();

        assertEquals(expected, actual);
    }

    @Test
    public void testAddUserTechnology ()
    {
        UserTechnology userTechnology = new UserTechnology(getUser(), getTechnology(), 2);
        userTechnology = userTechnologyRepository.save(userTechnology);

        UserTechnology expected = getUserTechnology(2, 3);
        UserTechnology actual = userTechnologyRepository.findById(userTechnology.getId()).orElse(null);

        assertEquals(expected, actual);
    }

    @Test
    public void testUpdateUserTechnology ()
    {
        UserTechnology userTechnology = userTechnologyRepository.findById(2).orElse(null);
        if (userTechnology != null)
        {
            userTechnology.setLevel(3);
            userTechnologyRepository.save(userTechnology);
        }

        UserTechnology expected = getUserTechnology(3, 2);
        UserTechnology actual = userTechnologyRepository.findById(2).orElse(null);

        assertEquals(expected, actual);
    }

    @Test
    public void testDeleteUserTechnology ()
    {
        userTechnologyRepository.findById(2).ifPresent(
                userTechnology -> userTechnologyRepository.delete(userTechnology));

        UserTechnology actual = userTechnologyRepository.findById(2).orElse(null);

        assertNull(actual);
    }
}