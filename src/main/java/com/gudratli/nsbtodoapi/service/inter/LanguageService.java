package com.gudratli.nsbtodoapi.service.inter;

import com.gudratli.nsbtodoapi.entity.Language;
import com.gudratli.nsbtodoapi.exception.duplicate.DuplicateLanguageException;
import org.springframework.stereotype.Service;

import java.util.List;

public interface LanguageService
{
    List<Language> getAll ();

    List<Language> getByNameContaining (String name);

    Language getById (Integer id);

    Language getByName (String name);

    Language add (Language language) throws DuplicateLanguageException;

    Language update (Language language) throws DuplicateLanguageException;

    void remove (Integer id);
}
