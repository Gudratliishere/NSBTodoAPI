package com.gudratli.nsbtodoapi.service.inter;

import com.gudratli.nsbtodoapi.entity.Status;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface StatusService
{
    List<Status> getAll ();

    Status getById (Integer id);

    Status getByName (String name);

    Status add (Status status);

    Status update (Status status);

    void remove (Integer id);
}
