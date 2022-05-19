package com.gudratli.nsbtodoapi.repository;

import com.gudratli.nsbtodoapi.entity.Status;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StatusRepository extends JpaRepository<Status, Integer>
{
    Status findByName (String name);
}