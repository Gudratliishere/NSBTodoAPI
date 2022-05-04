package com.gudratli.nsbtodoapi.repository;

import com.gudratli.nsbtodoapi.entity.Language;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LanguageRepository extends JpaRepository<Language, Integer>
{
}