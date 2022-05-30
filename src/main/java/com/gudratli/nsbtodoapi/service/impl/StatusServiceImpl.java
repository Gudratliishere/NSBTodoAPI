package com.gudratli.nsbtodoapi.service.impl;

import com.gudratli.nsbtodoapi.entity.Status;
import com.gudratli.nsbtodoapi.exception.duplicate.DuplicateStatusException;
import com.gudratli.nsbtodoapi.repository.StatusRepository;
import com.gudratli.nsbtodoapi.service.inter.StatusService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class StatusServiceImpl implements StatusService
{
    private final StatusRepository statusRepository;

    @Override
    public List<Status> getAll ()
    {
        return statusRepository.findAll();
    }

    @Override
    public Status getById (Integer id)
    {
        return statusRepository.findById(id).orElse(null);
    }

    @Override
    public Status getByName (String name)
    {
        return statusRepository.findByName(name);
    }

    @Override
    public Status add (Status status) throws DuplicateStatusException
    {
        checkForDuplicate(status);

        return statusRepository.save(status);
    }

    @Override
    public Status update (Status status) throws DuplicateStatusException
    {
        checkForDuplicate(status);

        return statusRepository.save(status);
    }

    @Override
    public void remove (Integer id)
    {
        statusRepository.findById(id).ifPresent(statusRepository::delete);
    }

    private void checkForDuplicate (Status status) throws DuplicateStatusException
    {
        Status test = getByName(status.getName());

        if (test != null && !test.getId().equals(status.getId()))
            throw new DuplicateStatusException();
    }
}
