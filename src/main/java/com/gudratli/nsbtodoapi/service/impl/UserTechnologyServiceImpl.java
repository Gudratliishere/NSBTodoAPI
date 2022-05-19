package com.gudratli.nsbtodoapi.service.impl;

import com.gudratli.nsbtodoapi.entity.UserTechnology;
import com.gudratli.nsbtodoapi.exception.duplicate.DuplicateUserTechnologyException;
import com.gudratli.nsbtodoapi.repository.TechnologyRepository;
import com.gudratli.nsbtodoapi.repository.UserRepository;
import com.gudratli.nsbtodoapi.repository.UserTechnologyRepository;
import com.gudratli.nsbtodoapi.service.inter.UserTechnologyService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserTechnologyServiceImpl implements UserTechnologyService
{
    private final UserTechnologyRepository userTechnologyRepository;
    private final UserRepository userRepository;
    private final TechnologyRepository technologyRepository;

    public UserTechnologyServiceImpl (
            UserTechnologyRepository userTechnologyRepository,
            UserRepository userRepository, TechnologyRepository technologyRepository)
    {
        this.userTechnologyRepository = userTechnologyRepository;
        this.userRepository = userRepository;
        this.technologyRepository = technologyRepository;
    }

    @Override
    public List<UserTechnology> getAll ()
    {
        return userTechnologyRepository.findAll();
    }

    @Override
    public List<UserTechnology> getByUserId (Integer id)
    {
        return userTechnologyRepository.findByUser(userRepository.findById(id).orElse(null));
    }

    @Override
    public List<UserTechnology> getByTechnologyId (Integer id)
    {
        return userTechnologyRepository.findByTechnology(technologyRepository.findById(id).orElse(null));
    }

    @Override
    public UserTechnology getById (Integer id)
    {
        return userTechnologyRepository.findById(id).orElse(null);
    }

    @Override
    public UserTechnology add (UserTechnology userTechnology) throws DuplicateUserTechnologyException
    {
        checkForDuplicate(userTechnology);

        return userTechnologyRepository.save(userTechnology);
    }

    @Override
    public UserTechnology update (UserTechnology userTechnology) throws DuplicateUserTechnologyException
    {
        checkForDuplicate(userTechnology);

        return userTechnologyRepository.save(userTechnology);
    }

    @Override
    public void remove (Integer id)
    {
        userTechnologyRepository.findById(id).ifPresent(userTechnologyRepository::delete);
    }

    private void checkForDuplicate (UserTechnology userTechnology) throws DuplicateUserTechnologyException
    {
        UserTechnology test = userTechnologyRepository.findByUserAndTechnology(userTechnology.getUser(),
                userTechnology.getTechnology());

        if (test != null && !test.getId().equals(userTechnology.getId()))
            throw new DuplicateUserTechnologyException();
    }
}
