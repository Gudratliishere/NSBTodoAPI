package com.gudratli.nsbtodoapi.service.impl;

import com.gudratli.nsbtodoapi.entity.Country;
import com.gudratli.nsbtodoapi.exception.duplicate.DuplicateCountryException;
import com.gudratli.nsbtodoapi.repository.CountryRepository;
import com.gudratli.nsbtodoapi.repository.RegionRepository;
import com.gudratli.nsbtodoapi.service.inter.CountryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class CountryServiceImpl implements CountryService
{
    private final CountryRepository countryRepository;
    private final RegionRepository regionRepository;

    @Override
    public List<Country> getAll ()
    {
        return countryRepository.findAll();
    }

    @Override
    public List<Country> getByNameContaining (String name)
    {
        return countryRepository.findByNameContaining(name);
    }

    @Override
    public List<Country> getByRegionId (Integer id)
    {
        return countryRepository.findByRegion(regionRepository.findById(id).orElse(null));
    }

    @Override
    public Country getById (Integer id)
    {
        return countryRepository.findById(id).orElse(null);
    }

    @Override
    public Country getByName (String name)
    {
        List<Country> countries = countryRepository.findByName(name);
        return (countries.size() > 0) ? countries.get(0) : null;
    }

    @Override
    public Country add (Country country) throws DuplicateCountryException
    {
        checkForDuplicate(country);

        return countryRepository.save(country);
    }

    @Override
    public Country update (Country country) throws DuplicateCountryException
    {
        checkForDuplicate(country);

        return countryRepository.save(country);
    }

    @Override
    public void remove (Integer id)
    {
        countryRepository.findById(id).ifPresent(countryRepository::delete);
    }

    private void checkForDuplicate (Country country) throws DuplicateCountryException
    {
        Country test = countryRepository.findByNameAndRegion(country.getName(), country.getRegion());

        if (test != null && !test.getId().equals(country.getId()))
            throw new DuplicateCountryException();
    }
}
