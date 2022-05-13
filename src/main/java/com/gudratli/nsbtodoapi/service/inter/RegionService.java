package com.gudratli.nsbtodoapi.service.inter;

import com.gudratli.nsbtodoapi.entity.Region;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface RegionService
{
    List<Region> getAll ();

    List<Region> getByNameContaining (String name);

    Region getById (Integer id);

    Region getByName (String name);

    Region add (Region region);

    Region update (Region region);

    void remove (Integer id);
}
