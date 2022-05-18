package com.gudratli.nsbtodoapi.service.impl;

import com.gudratli.nsbtodoapi.entity.Technology;
import com.gudratli.nsbtodoapi.repository.TechnologyRepository;
import com.gudratli.nsbtodoapi.service.inter.TechnologyService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TechnologyServiceImpl implements TechnologyService
{
    private final TechnologyRepository technologyRepository;

    public TechnologyServiceImpl (
            TechnologyRepository technologyRepository)
    {this.technologyRepository = technologyRepository;}

    @Override
    public List<Technology> getAll ()
    {
        return technologyRepository.findAll();
    }

    @Override
    public List<Technology> getByNameContaining (String name)
    {
        return technologyRepository.findByNameContaining(name);
    }

    @Override
    public Technology getById (Integer id)
    {
        return technologyRepository.findById(id).orElse(null);
    }

    @Override
    public Technology getByName (String name)
    {
        return technologyRepository.findByNameContaining(name).get(0);
    }

    @Override
    public Technology add (Technology technology)
    {
        return technologyRepository.save(technology);
    }

    @Override
    public Technology update (Technology technology)
    {
        return technologyRepository.save(technology);
    }

    @Override
    public void remove (Integer id)
    {
        technologyRepository.findById(id).ifPresent(technologyRepository::delete);
    }
}
