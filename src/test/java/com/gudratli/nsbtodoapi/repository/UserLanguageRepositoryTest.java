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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

@ExtendWith(SpringExtension.class)
@Transactional
@SpringBootTest(classes = NsbTodoApiApplication.class)
class UserLanguageRepositoryTest
{

    @Autowired
    private UserLanguageRepository userLanguageRepository;

    @Test
    public void testFindById ()
    {
        UserLanguage expected = getUserLanguage();
        UserLanguage actual = userLanguageRepository.findById(1).orElse(null);

        assertEquals(expected, actual);
    }

    @Test
    public void testFindByUser ()
    {
        List<UserLanguage> expected = getUserLanguages();
        List<UserLanguage> actual = userLanguageRepository.findByUser(getUser());

        assertEquals(expected, actual);
    }

    @Test
    public void testFindByLanguage ()
    {
        List<UserLanguage> expected = getUserLanguages();
        List<UserLanguage> actual = userLanguageRepository.findByLanguage(getLanguage());

        assertEquals(expected, actual);
    }

    @Test
    public void testFindAll ()
    {
        List<UserLanguage> expected = getUserLanguages();
        List<UserLanguage> actual = userLanguageRepository.findAll();

        assertEquals(expected, actual);
    }

    @Test
    public void testAddUserLanguage ()
    {
        UserLanguage userLanguage = new UserLanguage(getUser(), getLanguage(), 5);
        userLanguage = userLanguageRepository.save(userLanguage);

        UserLanguage actual = userLanguageRepository.findById(userLanguage.getId()).orElse(null);
        UserLanguage expected = getUserLanguage(5, 3);

        assertEquals(expected, actual);
    }

    @Test
    public void testUpdateUserLanguage ()
    {
        UserLanguage userLanguage = userLanguageRepository.findById(2).orElse(null);
        if (userLanguage != null)
        {
            userLanguage.setLevel(3);
            userLanguageRepository.save(userLanguage);
        }

        UserLanguage expected = getUserLanguage(3, 2);
        UserLanguage actual = userLanguageRepository.findById(2).orElse(null);

        assertEquals(expected, actual);
    }

    @Test
    public void testDeleteUserLanguage ()
    {
        userLanguageRepository.findById(2).ifPresent(userLanguage -> userLanguageRepository.delete(userLanguage));

        UserLanguage actual = userLanguageRepository.findById(2).orElse(null);

        assertNull(actual);
    }

    private UserLanguage getUserLanguage ()
    {
        UserLanguage userLanguage = new UserLanguage(getUser(), getLanguage(), 8);
        userLanguage.setId(1);
        return userLanguage;
    }

    private UserLanguage getUserLanguage (Integer level, int id)
    {
        UserLanguage userLanguage = new UserLanguage(getUser(), getLanguage(), level);
        userLanguage.setId(id);
        return userLanguage;
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

    private Language getLanguage ()
    {
        Language language = new Language("Turkish");
        language.setId(1);
        return language;
    }

    private List<UserLanguage> getUserLanguages ()
    {
        return Arrays.asList(getUserLanguage(), getUserLanguage(10, 2));
    }
}