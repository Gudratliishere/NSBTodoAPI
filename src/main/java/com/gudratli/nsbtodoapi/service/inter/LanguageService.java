package com.gudratli.nsbtodoapi.service.inter;

import com.gudratli.nsbtodoapi.entity.Language;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface LanguageService
{
    List<Language> getAll ();

    List<Language> getByNameContaining (String name);

    Language getById (Integer id);

    Language getByName (String name);

    Language add (Language language);

    Language update (Language language);

    void remove (Integer id);
}
