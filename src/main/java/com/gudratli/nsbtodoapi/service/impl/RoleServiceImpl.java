package com.gudratli.nsbtodoapi.service.impl;

import com.gudratli.nsbtodoapi.entity.Role;
import com.gudratli.nsbtodoapi.exception.duplicate.DuplicateRoleException;
import com.gudratli.nsbtodoapi.repository.RoleRepository;
import com.gudratli.nsbtodoapi.service.inter.RoleService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoleServiceImpl implements RoleService
{
    private final RoleRepository roleRepository;

    public RoleServiceImpl (RoleRepository roleRepository) {this.roleRepository = roleRepository;}

    @Override
    public List<Role> getAll ()
    {
        return roleRepository.findAll();
    }

    @Override
    public Role getById (Integer id)
    {
        return roleRepository.findById(id).orElse(null);
    }

    @Override
    public Role getByName (String name)
    {
        return roleRepository.findByName(name);
    }

    @Override
    public Role add (Role role) throws DuplicateRoleException
    {
        checkForDuplicate(role);

        return roleRepository.save(role);
    }

    @Override
    public Role update (Role role) throws DuplicateRoleException
    {
        checkForDuplicate(role);

        return roleRepository.save(role);
    }

    @Override
    public void remove (Integer id)
    {
        roleRepository.findById(id).ifPresent(roleRepository::delete);
    }

    private void checkForDuplicate (Role role) throws DuplicateRoleException
    {
        Role test = getByName(role.getName());

        if (test != null && !test.getId().equals(role.getId()))
            throw new DuplicateRoleException();
    }
}
