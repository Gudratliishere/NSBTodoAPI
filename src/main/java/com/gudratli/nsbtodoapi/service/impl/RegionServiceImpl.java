package com.gudratli.nsbtodoapi.service.impl;

import com.gudratli.nsbtodoapi.entity.Region;
import com.gudratli.nsbtodoapi.repository.RegionRepository;
import com.gudratli.nsbtodoapi.service.inter.RegionService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RegionServiceImpl implements RegionService
{
    private final RegionRepository regionRepository;

    public RegionServiceImpl (RegionRepository regionRepository) {this.regionRepository = regionRepository;}

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
        return regionRepository.findByNameContaining(name).get(0);
    }

    @Override
    public Region add (Region region)
    {
        return regionRepository.save(region);
    }

    @Override
    public Region update (Region region)
    {
        return regionRepository.save(region);
    }

    @Override
    public void remove (Integer id)
    {
        regionRepository.findById(id).ifPresent(regionRepository::delete);
    }
}
