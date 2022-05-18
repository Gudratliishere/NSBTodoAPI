package com.gudratli.nsbtodoapi.service.impl;

import com.gudratli.nsbtodoapi.entity.Language;
import com.gudratli.nsbtodoapi.repository.LanguageRepository;
import com.gudratli.nsbtodoapi.service.inter.LanguageService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LanguageServiceImpl implements LanguageService
{
    private final LanguageRepository languageRepository;

    public LanguageServiceImpl (LanguageRepository languageRepository) {this.languageRepository = languageRepository;}

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
        return languageRepository.findByNameContaining(name).get(0);
    }

    @Override
    public Language add (Language language)
    {
        return languageRepository.save(language);
    }

    @Override
    public Language update (Language language)
    {
        return languageRepository.save(language);
    }

    @Override
    public void remove (Integer id)
    {
        languageRepository.findById(id).ifPresent(languageRepository::delete);
    }
}
