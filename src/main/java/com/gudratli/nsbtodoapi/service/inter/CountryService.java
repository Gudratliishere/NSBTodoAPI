package com.gudratli.nsbtodoapi.service.inter;

import com.gudratli.nsbtodoapi.entity.Country;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface CountryService
{
    List<Country> getAll ();

    List<Country> getByNameContaining (String name);

    List<Country> getByRegionId (Integer id);

    Country getById (Integer id);

    Country getByName (String name);

    Country add (Country country);

    Country update (Country country);

    void remove (Integer id);
}
