package com.gudratli.nsbtodoapi.service.impl;

import com.gudratli.nsbtodoapi.entity.Language;
import com.gudratli.nsbtodoapi.exception.duplicate.DuplicateLanguageException;
import com.gudratli.nsbtodoapi.repository.LanguageRepository;
import com.gudratli.nsbtodoapi.service.inter.LanguageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class LanguageServiceImpl implements LanguageService
{
    private final LanguageRepository languageRepository;

    @Override
    public List<Language> getAll ()
    {
        return languageRepository.findAll();
    }

    @Override
    public List<Language> getByNameContaining (String name)
    {
        return languageRepository.findByNameContaining(name);
    }

    @Override
    public Language getById (Integer id)
    {
        return languageRepository.findById(id).orElse(null);
    }

    @Override
    public Language getByName (String name)
    {
        List<Language> languages = languageRepository.findByNameContaining(name);
        return (languages.size() > 0) ? languages.get(0) : null;
    }

    @Override
    public Language add (Language language) throws DuplicateLanguageException
    {
        checkForDuplicate(language);

        return languageRepository.save(language);
    }

    @Override
    public Language update (Language language) throws DuplicateLanguageException
    {
        checkForDuplicate(language);

        return languageRepository.save(language);
    }

    @Override
    public void remove (Integer id)
    {
        languageRepository.findById(id).ifPresent(languageRepository::delete);
    }

    private void checkForDuplicate (Language language) throws DuplicateLanguageException
    {
        Language test = getByName(language.getName());

        if (test != null && !test.getId().equals(language.getId()))
            throw new DuplicateLanguageException();
    }
}
