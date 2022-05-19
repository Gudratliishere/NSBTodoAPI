package com.gudratli.nsbtodoapi.service.inter;

import com.gudratli.nsbtodoapi.entity.Status;
import com.gudratli.nsbtodoapi.exception.duplicate.DuplicateStatusException;
import org.springframework.stereotype.Service;

import java.util.List;

public interface StatusService
{
    List<Status> getAll ();

    Status getById (Integer id);

    Status getByName (String name);

    Status add (Status status) throws DuplicateStatusException;

    Status update (Status status) throws DuplicateStatusException;

    void remove (Integer id);
}
