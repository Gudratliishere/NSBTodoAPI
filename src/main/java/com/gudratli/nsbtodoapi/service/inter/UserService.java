package com.gudratli.nsbtodoapi.service.inter;

import com.gudratli.nsbtodoapi.entity.User;
import com.gudratli.nsbtodoapi.exception.duplicate.DuplicateEmailException;
import com.gudratli.nsbtodoapi.exception.duplicate.DuplicatePhoneException;
import com.gudratli.nsbtodoapi.exception.duplicate.DuplicateUsernameException;

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

    User update (User user) throws DuplicatePhoneException, DuplicateEmailException, DuplicateUsernameException;

    User changePassword (Integer id, String password);

    User changeStatus (Integer id, Boolean status);

    User changeBanned (Integer id, Boolean banned);

    User changeRole (Integer id, Integer roleId);

    void remove (Integer id);
}
