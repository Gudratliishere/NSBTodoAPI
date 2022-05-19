package com.gudratli.nsbtodoapi.service.inter;

import com.gudratli.nsbtodoapi.entity.Country;
import com.gudratli.nsbtodoapi.exception.duplicate.DuplicateCountryException;
import org.springframework.stereotype.Service;

import java.util.List;

public interface CountryService
{
    List<Country> getAll ();

    List<Country> getByNameContaining (String name);

    List<Country> getByRegionId (Integer id);

    Country getById (Integer id);

    Country getByName (String name);

    Country add (Country country) throws DuplicateCountryException;

    Country update (Country country) throws DuplicateCountryException;

    void remove (Integer id);
}
