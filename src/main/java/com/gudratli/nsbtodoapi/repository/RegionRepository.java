package com.gudratli.nsbtodoapi.repository;

import com.gudratli.nsbtodoapi.entity.Region;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RegionRepository extends JpaRepository<Region, Integer>
{
    List<Region> findByName (String name);

    List<Region> findByNameContaining (String name);
}