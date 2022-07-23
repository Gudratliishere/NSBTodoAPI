package com.gudratli.nsbtodoapi.repository;

import com.gudratli.nsbtodoapi.entity.Country;
import com.gudratli.nsbtodoapi.entity.Region;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CountryRepository extends JpaRepository<Country, Integer>
{
    List<Country> findByName (String name);

    List<Country> findByNameContaining (String name);

    List<Country> findByRegion (Region region);

    Country findByNameAndRegion (String name, Region region);
}