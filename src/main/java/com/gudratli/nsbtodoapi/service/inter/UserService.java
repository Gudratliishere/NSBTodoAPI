package com.gudratli.nsbtodoapi.service.inter;

import com.gudratli.nsbtodoapi.entity.User;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface UserService
{
    List<User> getAll ();

    List<User> getByNameContaining (String name);

    List<User> getBySurnameContaining (String surname);

    List<User> getByCountryId (Integer id);

    List<User> getByRoleId (Integer id);

    User getById (Integer id);

    User getByPhone (String phone);

    User getByEmail (String email);

    User getByUsername (String username);

    User add (User user);

    User update (User user);

    void remove (Integer id);
}
