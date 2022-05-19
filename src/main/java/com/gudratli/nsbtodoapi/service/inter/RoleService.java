package com.gudratli.nsbtodoapi.service.inter;

import com.gudratli.nsbtodoapi.entity.Role;
import com.gudratli.nsbtodoapi.exception.duplicate.DuplicateRoleException;
import org.springframework.stereotype.Service;

import java.util.List;

public interface RoleService
{
    List<Role> getAll ();

    Role getById (Integer id);

    Role getByName (String name);

    Role add (Role role) throws DuplicateRoleException;

    Role update (Role role) throws DuplicateRoleException;

    void remove (Integer id);
}
