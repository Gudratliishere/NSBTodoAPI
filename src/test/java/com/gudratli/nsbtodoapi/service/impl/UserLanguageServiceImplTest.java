package com.gudratli.nsbtodoapi.service.impl;

import com.gudratli.nsbtodoapi.entity.Language;
import com.gudratli.nsbtodoapi.entity.User;
import com.gudratli.nsbtodoapi.entity.UserLanguage;
import com.gudratli.nsbtodoapi.exception.duplicate.DuplicateUserLanguageException;
import com.gudratli.nsbtodoapi.repository.LanguageRepository;
import com.gudratli.nsbtodoapi.repository.UserLanguageRepository;
import com.gudratli.nsbtodoapi.repository.UserRepository;
import com.gudratli.nsbtodoapi.service.inter.UserLanguageService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static com.gudratli.nsbtodoapi.util.Entities.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserLanguageServiceImplTest
{
    private UserLanguageRepository userLanguageRepository;
    private UserRepository userRepository;
    private LanguageRepository languageRepository;

    private UserLanguageService userLanguageService;

    @BeforeEach
    public void setUp ()
    {
        userLanguageRepository = mock(UserLanguageRepository.class);
        userRepository = mock(UserRepository.class);
        languageRepository = mock(LanguageRepository.class);

        userLanguageService = new UserLanguageServiceImpl(userLanguageRepository, userRepository, languageRepository);
    }

    @Test
    public void testGetAll ()
    {
        List<UserLanguage> expected = getUserLanguageList();
        when(userLanguageRepository.findAll()).thenReturn(expected);

        List<UserLanguage> actual = userLanguageService.getAll();

        assertEquals(expected, actual);
        verify(userLanguageRepository).findAll();
    }

    @Test
    public void testGetByUserId ()
    {
        List<UserLanguage> expected = getUserLanguageList();
        User user = getUser();
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        when(userLanguageRepository.findByUser(user)).thenReturn(expected);

        List<UserLanguage> actual = userLanguageService.getByUserId(user.getId());

        assertEquals(expected, actual);
        verify(userRepository).findById(user.getId());
        verify(userLanguageRepository).findByUser(user);
    }

    @Test
    public void testGetByLanguageId ()
    {
        List<UserLanguage> expected = getUserLanguageList();
        Language language = getLanguage();
        when(languageRepository.findById(language.getId())).thenReturn(Optional.of(language));
        when(userLanguageRepository.findByLanguage(language)).thenReturn(expected);

        List<UserLanguage> actual = userLanguageService.getByLanguageId(language.getId());

        assertEquals(expected, actual);
        verify(languageRepository).findById(language.getId());
        verify(userLanguageRepository).findByLanguage(language);
    }

    @Test
    public void testGetById ()
    {
        UserLanguage expected = getUserLanguage();
        when(userLanguageRepository.findById(expected.getId())).thenReturn(Optional.of(expected));

        UserLanguage actual = userLanguageService.getById(expected.getId());

        assertEquals(expected, actual);
        verify(userLanguageRepository).findById(expected.getId());
    }

    @Test
    public void testAdd_whenValidUserLanguage () throws DuplicateUserLanguageException
    {
        User user = getUser();
        Language language = getLanguage();
        UserLanguage userLanguage = new UserLanguage(user, language, 3);
        UserLanguage expected = getUserLanguage(3, 5);
        when(userLanguageRepository.save(userLanguage)).thenReturn(expected);
        when(userLanguageRepository.findByUserAndLanguage(user, language)).thenReturn(null);

        UserLanguage actual = userLanguageService.add(userLanguage);

        assertEquals(expected, actual);
        verify(userLanguageRepository).save(userLanguage);
        verify(userLanguageRepository).findByUserAndLanguage(user, language);
    }

    @Test
    public void testAdd_whenDuplicateUserLanguage ()
    {
        User user = getUser();
        Language language = getLanguage();
        UserLanguage userLanguage = new UserLanguage(user, language, 3);
        UserLanguage expected = getUserLanguage(3, 5);
        when(userLanguageRepository.findByUserAndLanguage(user, language)).thenReturn(expected);

        try
        {
            userLanguageService.add(userLanguage);
        } catch (DuplicateUserLanguageException e)
        {
            assertInstanceOf(DuplicateUserLanguageException.class, e);
        }

        verify(userLanguageRepository).findByUserAndLanguage(user, language);
    }

    @Test
    public void testUpdate_whenValidUserLanguage () throws DuplicateUserLanguageException
    {
        User user = getUser();
        Language language = getLanguage();
        UserLanguage userLanguage = getUserLanguage(3, 4);
        UserLanguage expected = getUserLanguage(3, userLanguage.getId() + 1);
        when(userLanguageRepository.save(userLanguage)).thenReturn(expected);
        when(userLanguageRepository.findByUserAndLanguage(user, language)).thenReturn(null);

        UserLanguage actual = userLanguageService.update(userLanguage);

        assertEquals(expected, actual);
        verify(userLanguageRepository).save(userLanguage);
        verify(userLanguageRepository).findByUserAndLanguage(user, language);
    }

    @Test
    public void testUpdate_whenDuplicateUserLanguage ()
    {
        User user = getUser();
        Language language = getLanguage();
        UserLanguage userLanguage = getUserLanguage(3, 4);
        UserLanguage expected = getUserLanguage(3, userLanguage.getId() + 1);
        when(userLanguageRepository.findByUserAndLanguage(user, language)).thenReturn(expected);

        try
        {
            userLanguageService.update(userLanguage);
        } catch (DuplicateUserLanguageException e)
        {
            assertInstanceOf(DuplicateUserLanguageException.class, e);
        }

        verify(userLanguageRepository).findByUserAndLanguage(user, language);
    }

    @Test
    public void testRemove ()
    {
        UserLanguage userLanguage = getUserLanguage();
        when(userLanguageRepository.findById(userLanguage.getId())).thenReturn(Optional.of(userLanguage));

        userLanguageService.remove(userLanguage.getId());

        when(userLanguageRepository.findById(userLanguage.getId())).thenReturn(Optional.empty());

        UserLanguage actual = userLanguageService.getById(userLanguage.getId());

        assertNull(actual);
    }
}