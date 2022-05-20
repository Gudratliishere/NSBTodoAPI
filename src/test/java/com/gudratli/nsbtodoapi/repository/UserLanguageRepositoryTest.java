package com.gudratli.nsbtodoapi.repository;

import com.gudratli.nsbtodoapi.NsbTodoApiApplication;
import com.gudratli.nsbtodoapi.entity.UserLanguage;
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
        List<UserLanguage> expected = getUserLanguageList();
        List<UserLanguage> actual = userLanguageRepository.findByUser(getUser());

        assertEquals(expected, actual);
    }

    @Test
    public void testFindByLanguage ()
    {
        List<UserLanguage> expected = getUserLanguageList();
        List<UserLanguage> actual = userLanguageRepository.findByLanguage(getLanguage());

        assertEquals(expected, actual);
    }

    @Test
    public void testFindAll ()
    {
        List<UserLanguage> expected = getUserLanguageList();
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
}