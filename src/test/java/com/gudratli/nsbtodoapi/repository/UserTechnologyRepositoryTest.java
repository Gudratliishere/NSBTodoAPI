package com.gudratli.nsbtodoapi.repository;

import com.gudratli.nsbtodoapi.NsbTodoApiApplication;
import com.gudratli.nsbtodoapi.entity.*;
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
        List<UserTechnology> expected = getUserTechnologies();
        List<UserTechnology> actual = userTechnologyRepository.findByUser(getUser());

        assertEquals(expected, actual);
    }

    @Test
    public void testFindByTechnology ()
    {
        List<UserTechnology> expected = getUserTechnologies();
        List<UserTechnology> actual = userTechnologyRepository.findByTechnology(getTechnology());

        assertEquals(expected, actual);
    }

    @Test
    public void testFindAll ()
    {
        List<UserTechnology> expected = getUserTechnologies();
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
        userTechnologyRepository.findById(2).ifPresent(userTechnology -> userTechnologyRepository.delete(userTechnology));

        UserTechnology actual = userTechnologyRepository.findById(2).orElse(null);

        assertNull(actual);
    }

    private UserTechnology getUserTechnology ()
    {
        UserTechnology userTechnology = new UserTechnology(getUser(), getTechnology(), 5);
        userTechnology.setId(1);
        return userTechnology;
    }

    private UserTechnology getUserTechnology (Integer level, int id)
    {
        UserTechnology userTechnology = new UserTechnology(getUser(), getTechnology(), level);
        userTechnology.setId(id);
        return userTechnology;
    }

    private User getUser ()
    {
        User user = new User("Dunay", "Gudratli", "0556105884", "dunay@gmail", "git",
                "masazir", "cv", "dunay", "123", getCountry(), getRole());
        user.setId(6);
        user.setStatus(true);
        user.setBanned(false);
        return user;
    }

    private Role getRole ()
    {
        Role role = new Role("USER", "User");
        role.setId(1);
        return role;
    }

    private Country getCountry ()
    {
        Country country = new Country("UK", getRegion());
        country.setId(1);
        return country;
    }

    private Region getRegion ()
    {
        Region region = new Region("Asia");
        region.setId(1);
        return region;
    }

    private Technology getTechnology ()
    {
        Technology technology = new Technology("HTML");
        technology.setId(1);
        return technology;
    }

    private List<UserTechnology> getUserTechnologies ()
    {
        return Arrays.asList(getUserTechnology(), getUserTechnology(7, 2));
    }
}