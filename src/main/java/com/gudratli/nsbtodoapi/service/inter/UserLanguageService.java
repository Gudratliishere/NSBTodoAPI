package com.gudratli.nsbtodoapi.service.inter;

import com.gudratli.nsbtodoapi.entity.UserLanguage;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface UserLanguageService
{
    List<UserLanguage> getAll ();

    List<UserLanguage> getByUserId (Integer id);

    List<UserLanguage> getByLanguageId (Integer id);

    UserLanguage getById (Integer id);

    UserLanguage add (UserLanguage userLanguage);

    UserLanguage update (UserLanguage userLanguage);

    void remove (Integer id);
}
