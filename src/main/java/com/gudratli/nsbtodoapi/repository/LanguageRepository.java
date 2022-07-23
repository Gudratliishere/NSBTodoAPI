package com.gudratli.nsbtodoapi.repository;

import com.gudratli.nsbtodoapi.entity.Language;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LanguageRepository extends JpaRepository<Language, Integer>
{
    List<Language> findByName (String name);

    List<Language> findByNameContaining (String name);
}