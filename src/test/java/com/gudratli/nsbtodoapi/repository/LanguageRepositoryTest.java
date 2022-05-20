package com.gudratli.nsbtodoapi.repository;

import com.gudratli.nsbtodoapi.NsbTodoApiApplication;
import com.gudratli.nsbtodoapi.entity.Language;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.transaction.Transactional;
import java.util.List;

import static com.gudratli.nsbtodoapi.util.Entities.getLanguage;
import static com.gudratli.nsbtodoapi.util.Entities.getLanguageList;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

@ExtendWith(SpringExtension.class)
@Transactional
@SpringBootTest(classes = NsbTodoApiApplication.class)
class LanguageRepositoryTest
{
    @Autowired
    private LanguageRepository languageRepository;

    @Test
    public void testFindById ()
    {
        Language expected = getLanguage();
        Language actual = languageRepository.findById(1).orElse(null);

        assertEquals(expected, actual);
    }

    @Test
    public void testFindByNameContaining ()
    {
        Language language = getLanguage();
        Language actual = languageRepository.findByNameContaining("urk").get(0);

        assertEquals(language, actual);
    }

    @Test
    public void testFindAll ()
    {
        List<Language> expected = getLanguageList();
        List<Language> actual = languageRepository.findAll();

        assertEquals(expected, actual);
    }

    @Test
    public void testAddLanguage ()
    {
        Language language = new Language("Russian");

        language = languageRepository.save(language);

        Language expected = getLanguage("Russian", language.getId());
        Language actual = languageRepository.findById(language.getId()).orElse(null);

        assertEquals(expected, actual);
    }

    @Test
    public void testUpdateLanguage ()
    {
        Language actual = languageRepository.findById(2).orElse(null);
        if (actual != null)
        {
            actual.setName("EnglishUpdated");
            languageRepository.save(actual);
        }
        actual = languageRepository.findById(2).orElse(null);

        Language expected = getLanguage("EnglishUpdated", 2);

        assertEquals(expected, actual);
    }

    @Test
    public void testDeleteLanguage ()
    {
        languageRepository.findById(2).ifPresent(language1 -> languageRepository.delete(language1));

        Language actual = languageRepository.findById(2).orElse(null);

        assertNull(actual);
    }
}