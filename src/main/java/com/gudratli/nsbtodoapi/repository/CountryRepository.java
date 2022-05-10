package com.gudratli.nsbtodoapi.repository;

import com.gudratli.nsbtodoapi.entity.Country;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CountryRepository extends JpaRepository<Country, Integer>
{
    List<Country> findByNameContaining(String name);
}