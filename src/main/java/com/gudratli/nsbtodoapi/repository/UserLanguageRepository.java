package com.gudratli.nsbtodoapi.repository;

import com.gudratli.nsbtodoapi.entity.Language;
import com.gudratli.nsbtodoapi.entity.User;
import com.gudratli.nsbtodoapi.entity.UserLanguage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserLanguageRepository extends JpaRepository<UserLanguage, Integer>
{
    List<UserLanguage> findByUser (User user);
    List<UserLanguage> findByLanguage (Language language);
    UserLanguage findByUserAndLanguage (User user, Language language);
}