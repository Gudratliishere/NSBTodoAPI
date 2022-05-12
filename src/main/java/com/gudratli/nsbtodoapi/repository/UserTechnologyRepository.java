package com.gudratli.nsbtodoapi.repository;

import com.gudratli.nsbtodoapi.entity.Technology;
import com.gudratli.nsbtodoapi.entity.User;
import com.gudratli.nsbtodoapi.entity.UserTechnology;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserTechnologyRepository extends JpaRepository<UserTechnology, Integer>
{
    List<UserTechnology> findByUser (User user);

    List<UserTechnology> findByTechnology (Technology technology);
}