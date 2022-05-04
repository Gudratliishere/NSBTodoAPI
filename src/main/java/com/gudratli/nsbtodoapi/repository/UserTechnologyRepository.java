package com.gudratli.nsbtodoapi.repository;

import com.gudratli.nsbtodoapi.entity.UserTechnology;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserTechnologyRepository extends JpaRepository<UserTechnology, Integer>
{
}