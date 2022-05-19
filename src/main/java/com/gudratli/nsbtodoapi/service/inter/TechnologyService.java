package com.gudratli.nsbtodoapi.service.inter;

import com.gudratli.nsbtodoapi.entity.Technology;
import com.gudratli.nsbtodoapi.exception.duplicate.DuplicateTechnologyException;
import org.springframework.stereotype.Service;

import java.util.List;

public interface TechnologyService
{
    List<Technology> getAll ();

    List<Technology> getByNameContaining (String name);

    Technology getById (Integer id);

    Technology getByName (String name);

    Technology add (Technology technology) throws DuplicateTechnologyException;

    Technology update (Technology technology) throws DuplicateTechnologyException;

    void remove (Integer id);
}
