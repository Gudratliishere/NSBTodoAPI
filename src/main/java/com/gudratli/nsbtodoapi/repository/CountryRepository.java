package com.gudratli.nsbtodoapi.repository;

import com.gudratli.nsbtodoapi.entity.Country;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CountryRepository extends JpaRepository<Country, Integer>
{
}