package com.gudratli.nsbtodoapi.repository;

import com.gudratli.nsbtodoapi.entity.File;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FileRepository extends JpaRepository<File, Integer>
{
}