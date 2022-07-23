package com.gudratli.nsbtodoapi.service.impl;

import com.gudratli.nsbtodoapi.entity.Region;
import com.gudratli.nsbtodoapi.exception.duplicate.DuplicateRegionException;
import com.gudratli.nsbtodoapi.repository.RegionRepository;
import com.gudratli.nsbtodoapi.service.inter.RegionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class RegionServiceImpl implements RegionService
{
    private final RegionRepository regionRepository;

    @Override
    public List<Region> getAll ()
    {
        return regionRepository.findAll();
    }

    @Override
    public List<Region> getByNameContaining (String name)
    {
        return regionRepository.findByNameContaining(name);
    }

    @Override
    public Region getById (Integer id)
    {
        return regionRepository.findById(id).orElse(null);
    }

    @Override
    public Region getByName (String name)
    {
        List<Region> regions = regionRepository.findByName(name);
        return (regions.size() > 0) ? regions.get(0) : null;
    }

    @Override
    public Region add (Region region) throws DuplicateRegionException
    {
        checkForDuplicate(region);

        return regionRepository.save(region);
    }

    @Override
    public Region update (Region region) throws DuplicateRegionException
    {
        checkForDuplicate(region);

        return regionRepository.save(region);
    }

    @Override
    public void remove (Integer id)
    {
        regionRepository.findById(id).ifPresent(regionRepository::delete);
    }

    private void checkForDuplicate (Region region) throws DuplicateRegionException
    {
        Region test = getByName(region.getName());

        if (test != null && !test.getId().equals(region.getId()))
            throw new DuplicateRegionException();
    }
}
