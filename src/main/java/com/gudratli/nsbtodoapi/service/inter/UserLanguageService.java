package com.gudratli.nsbtodoapi.service.inter;

import com.gudratli.nsbtodoapi.entity.UserLanguage;
import com.gudratli.nsbtodoapi.exception.duplicate.DuplicateUserLanguageException;

import java.util.List;

public interface UserLanguageService
{
    List<UserLanguage> getAll ();

    List<UserLanguage> getByUserId (Integer id);

    List<UserLanguage> getByLanguageId (Integer id);

    UserLanguage getById (Integer id);

    UserLanguage add (UserLanguage userLanguage) throws DuplicateUserLanguageException;

    UserLanguage update (UserLanguage userLanguage) throws DuplicateUserLanguageException;

    void remove (Integer id);
}
