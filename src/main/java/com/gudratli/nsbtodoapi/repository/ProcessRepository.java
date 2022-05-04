package com.gudratli.nsbtodoapi.repository;

import com.gudratli.nsbtodoapi.entity.Process;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProcessRepository extends JpaRepository<Process, Integer>
{
}