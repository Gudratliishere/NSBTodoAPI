package com.gudratli.nsbtodoapi.service.impl;

import com.gudratli.nsbtodoapi.entity.Role;
import com.gudratli.nsbtodoapi.exception.duplicate.DuplicateRoleException;
import com.gudratli.nsbtodoapi.repository.RoleRepository;
import com.gudratli.nsbtodoapi.service.inter.RoleService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static com.gudratli.nsbtodoapi.util.Entities.getRole;
import static com.gudratli.nsbtodoapi.util.Entities.getRoleList;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class RoleServiceImplTest
{
    private RoleRepository roleRepository;

    private RoleService roleService;

    @BeforeEach
    public void setUp ()
    {
        roleRepository = mock(RoleRepository.class);

        roleService = new RoleServiceImpl(roleRepository);
    }

    @Test
    public void testGetAll ()
    {
        List<Role> expected = getRoleList();
        when(roleRepository.findAll()).thenReturn(expected);

        List<Role> actual = roleService.getAll();

        assertEquals(expected, actual);
        verify(roleRepository).findAll();
    }

    @Test
    public void testGetById ()
    {
        Role expected = getRole();
        when(roleRepository.findById(expected.getId())).thenReturn(Optional.of(expected));

        Role actual = roleService.getById(expected.getId());

        assertEquals(expected, actual);
        verify(roleRepository).findById(expected.getId());
    }

    @Test
    public void testGetByName ()
    {
        Role expected = getRole();
        when(roleRepository.findByName(expected.getName())).thenReturn(expected);

        Role actual = roleService.getByName(expected.getName());

        assertEquals(expected, actual);
        verify(roleRepository).findByName(expected.getName());
    }

    @Test
    public void testAdd_whenValidRole () throws DuplicateRoleException
    {
        Role role = new Role("mod", "will  be mod");
        Role expected = getRole(role.getName(), role.getRoleDescription(), 5);
        when(roleRepository.save(role)).thenReturn(expected);
        when(roleRepository.findByName(role.getName())).thenReturn(null);

        Role actual = roleService.add(role);

        assertEquals(expected, actual);
        verify(roleRepository).save(role);
        verify(roleRepository).findByName(role.getName());
    }

    @Test
    public void testAdd_whenDuplicateRole_itShouldThrowException ()
    {
        Role role = new Role("mod", "will  be mod");
        Role expected = getRole(role.getName(), role.getRoleDescription(), 5);
        when(roleRepository.findByName(role.getName())).thenReturn(expected);

        try
        {
            roleService.add(role);
        } catch (DuplicateRoleException e)
        {
            assertInstanceOf(DuplicateRoleException.class, e);
        }

        verify(roleRepository).findByName(role.getName());
    }

    @Test
    public void testUpdate_whenValidRole () throws DuplicateRoleException
    {
        Role role = getRole("mod", "will  be mod", 5);
        Role expected = getRole(role.getName(), role.getRoleDescription(), role.getId() + 1);
        when(roleRepository.save(role)).thenReturn(expected);
        when(roleRepository.findByName(role.getName())).thenReturn(null);

        Role actual = roleService.update(role);

        assertEquals(expected, actual);
        verify(roleRepository).save(role);
        verify(roleRepository).findByName(role.getName());
    }

    @Test
    public void testUpdate_whenDuplicateRole_itShouldThrowException ()
    {
        Role role = getRole("mod", "will  be mod", 5);
        Role expected = getRole(role.getName(), role.getRoleDescription(), role.getId() + 1);
        when(roleRepository.findByName(role.getName())).thenReturn(expected);

        try
        {
            roleService.update(role);
        } catch (DuplicateRoleException e)
        {
            assertInstanceOf(DuplicateRoleException.class, e);
        }

        verify(roleRepository).findByName(role.getName());
    }

    @Test
    public void testRemove ()
    {
        Role role = getRole();
        when(roleRepository.findById(role.getId())).thenReturn(Optional.of(role));

        roleService.remove(role.getId());

        when(roleRepository.findById(role.getId())).thenReturn(Optional.empty());

        Role actual = roleService.getById(role.getId());

        assertNull(actual);
    }
}