package com.gudratli.nsbtodoapi.repository;

import com.gudratli.nsbtodoapi.entity.Country;
import com.gudratli.nsbtodoapi.entity.Role;
import com.gudratli.nsbtodoapi.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Integer>
{
    List<User> findByNameContaining(String name);
    List<User> findBySurnameContaining(String surname);
    List<User> findByStatus (Boolean status);
    List<User> findByBanned (Boolean banned);
    List<User> findByCountry (Country country);
    List<User> findByRole (Role role);
    User findByPhone (String phone);
    User findByEmail (String email);
}
