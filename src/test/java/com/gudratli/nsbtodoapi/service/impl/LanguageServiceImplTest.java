package com.gudratli.nsbtodoapi.service.impl;

import com.gudratli.nsbtodoapi.entity.Language;
import com.gudratli.nsbtodoapi.exception.duplicate.DuplicateLanguageException;
import com.gudratli.nsbtodoapi.repository.LanguageRepository;
import com.gudratli.nsbtodoapi.service.inter.LanguageService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static com.gudratli.nsbtodoapi.util.Entities.getLanguage;
import static com.gudratli.nsbtodoapi.util.Entities.getLanguageList;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class LanguageServiceImplTest
{
    private LanguageRepository languageRepository;

    private LanguageService languageService;

    @BeforeEach
    public void setUp ()
    {
        languageRepository = mock(LanguageRepository.class);

        languageService = new LanguageServiceImpl(languageRepository);
    }

    @Test
    public void testGetAll ()
    {
        List<Language> expected = getLanguageList();
        when(languageRepository.findAll()).thenReturn(expected);

        List<Language> actual = languageService.getAll();

        assertEquals(expected, actual);
        verify(languageRepository).findAll();
    }

    @Test
    public void testGetByNameContaining ()
    {
        List<Language> expected = getLanguageList();
        when(languageRepository.findByNameContaining("tur")).thenReturn(expected);

        List<Language> actual = languageRepository.findByNameContaining("tur");

        assertEquals(expected, actual);
        verify(languageRepository).findByNameContaining("tur");
    }

    @Test
    public void testGetById ()
    {
        Language expected = getLanguage();
        when(languageRepository.findById(expected.getId())).thenReturn(Optional.of(expected));

        Language actual = languageService.getById(expected.getId());

        assertEquals(expected, actual);
        verify(languageRepository).findById(expected.getId());
    }

    @Test
    public void testGetByName ()
    {
        Language expected = getLanguage();
        when(languageRepository.findByName(expected.getName())).thenReturn(Collections.singletonList(expected));

        Language actual = languageService.getByName(expected.getName());

        assertEquals(expected, actual);
        verify(languageRepository).findByName(expected.getName());
    }

    @Test
    public void testAdd_whenValidLanguage () throws DuplicateLanguageException
    {
        Language language = new Language("Japan");
        Language expected = getLanguage(language.getName(), 4);
        when(languageRepository.save(language)).thenReturn(expected);
        when(languageRepository.findByName(expected.getName())).thenReturn(Collections.emptyList());

        Language actual = languageService.add(language);

        assertEquals(expected, actual);
        verify(languageRepository).save(language);
        verify(languageRepository).findByName(expected.getName());
    }

    @Test
    public void testAdd_whenDuplicateLanguage ()
    {
        Language language = new Language("Japan");
        Language expected = getLanguage(language.getName(), 4);
        when(languageRepository.findByName(expected.getName())).thenReturn(Collections.singletonList(expected));

        try
        {
            languageService.add(language);
        } catch (DuplicateLanguageException e)
        {
            assertInstanceOf(DuplicateLanguageException.class, e);
        }

        verify(languageRepository).findByName(expected.getName());
    }

    @Test
    public void testUpdate_whenValidLanguage () throws DuplicateLanguageException
    {
        Language language = getLanguage("Japan", 3);
        Language expected = getLanguage(language.getName(), 4 + 1);
        when(languageRepository.save(language)).thenReturn(expected);
        when(languageRepository.findByName(expected.getName())).thenReturn(Collections.emptyList());

        Language actual = languageService.update(language);

        assertEquals(expected, actual);
        verify(languageRepository).save(language);
        verify(languageRepository).findByName(expected.getName());
    }

    @Test
    public void testUpdate_whenDuplicateLanguage ()
    {
        Language language = getLanguage("Japan", 3);
        Language expected = getLanguage(language.getName(), 4);
        when(languageRepository.findByName(expected.getName())).thenReturn(Collections.singletonList(expected));

        try
        {
            languageService.update(language);
        } catch (DuplicateLanguageException e)
        {
            assertInstanceOf(DuplicateLanguageException.class, e);
        }

        verify(languageRepository).findByName(expected.getName());
    }

    @Test
    public void testRemove ()
    {
        Language language = getLanguage();
        when(languageRepository.findById(language.getId())).thenReturn(Optional.of(language));

        languageService.remove(language.getId());

        when(languageRepository.findById(language.getId())).thenReturn(Optional.empty());
        Language actual = languageService.getById(language.getId());

        assertNull(actual);
    }
}