package com.gudratli.nsbtodoapi.repository;

import com.gudratli.nsbtodoapi.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Integer>
{
}