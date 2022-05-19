package com.gudratli.nsbtodoapi.service.inter;

import com.gudratli.nsbtodoapi.entity.Region;
import com.gudratli.nsbtodoapi.exception.duplicate.DuplicateRegionException;
import org.springframework.stereotype.Service;

import java.util.List;

public interface RegionService
{
    List<Region> getAll ();

    List<Region> getByNameContaining (String name);

    Region getById (Integer id);

    Region getByName (String name);

    Region add (Region region) throws DuplicateRegionException;

    Region update (Region region) throws DuplicateRegionException;

    void remove (Integer id);
}
