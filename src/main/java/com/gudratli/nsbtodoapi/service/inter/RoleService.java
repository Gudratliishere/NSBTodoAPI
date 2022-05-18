package com.gudratli.nsbtodoapi.service.inter;

import com.gudratli.nsbtodoapi.entity.Role;
import org.springframework.stereotype.Service;

import java.util.List;

public interface RoleService
{
    List<Role> getAll ();

    Role getById (Integer id);

    Role getByName (String name);

    Role add (Role role);

    Role update (Role role);

    void remove (Integer id);
}
