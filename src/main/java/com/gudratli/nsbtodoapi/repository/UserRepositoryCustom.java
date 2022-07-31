package com.gudratli.nsbtodoapi.repository;

import com.gudratli.nsbtodoapi.entity.Role;
import com.gudratli.nsbtodoapi.entity.User;

public interface UserRepositoryCustom
{
    User changePassword (Integer id, String password);

    User changeStatus (Integer id, Boolean status);

    User changeBanned (Integer id, Boolean banned);

    User changeRole (Integer id, Role role);
}
