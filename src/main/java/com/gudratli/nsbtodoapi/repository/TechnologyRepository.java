package com.gudratli.nsbtodoapi.repository;

import com.gudratli.nsbtodoapi.entity.Technology;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TechnologyRepository extends JpaRepository<Technology, Integer>
{
    List<Technology> findByNameContaining (String name);
}