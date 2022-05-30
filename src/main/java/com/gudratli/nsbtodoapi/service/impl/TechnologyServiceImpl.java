package com.gudratli.nsbtodoapi.service.impl;

import com.gudratli.nsbtodoapi.entity.Technology;
import com.gudratli.nsbtodoapi.exception.duplicate.DuplicateTechnologyException;
import com.gudratli.nsbtodoapi.repository.TechnologyRepository;
import com.gudratli.nsbtodoapi.service.inter.TechnologyService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class TechnologyServiceImpl implements TechnologyService
{
    private final TechnologyRepository technologyRepository;

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
        List<Technology> technologies = technologyRepository.findByNameContaining(name);
        return (technologies.size() > 0) ? technologies.get(0) : null;
    }

    @Override
    public Technology add (Technology technology) throws DuplicateTechnologyException
    {
        checkForDuplicate(technology);

        return technologyRepository.save(technology);
    }

    @Override
    public Technology update (Technology technology) throws DuplicateTechnologyException
    {
        checkForDuplicate(technology);

        return technologyRepository.save(technology);
    }

    @Override
    public void remove (Integer id)
    {
        technologyRepository.findById(id).ifPresent(technologyRepository::delete);
    }

    private void checkForDuplicate (Technology technology) throws DuplicateTechnologyException
    {
        Technology test = getByName(technology.getName());

        if (test != null && !test.getId().equals(technology.getId()))
            throw new DuplicateTechnologyException();
    }
}
