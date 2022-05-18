package com.gudratli.nsbtodoapi.service.inter;

import com.gudratli.nsbtodoapi.entity.User;
import com.gudratli.nsbtodoapi.exception.DuplicateEmailException;
import com.gudratli.nsbtodoapi.exception.DuplicatePhoneException;
import com.gudratli.nsbtodoapi.exception.DuplicateUsernameException;
import org.springframework.stereotype.Service;

import java.util.List;

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

    User add (User user) throws DuplicatePhoneException, DuplicateEmailException, DuplicateUsernameException;

    User update (User user);

    void remove (Integer id);
}
