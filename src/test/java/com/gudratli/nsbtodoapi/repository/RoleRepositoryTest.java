package com.gudratli.nsbtodoapi.repository;

import com.gudratli.nsbtodoapi.NsbTodoApiApplication;
import com.gudratli.nsbtodoapi.entity.Role;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.transaction.Transactional;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

@ExtendWith(SpringExtension.class)
@Transactional
@SpringBootTest(classes = NsbTodoApiApplication.class)
class RoleRepositoryTest
{
    @Autowired
    private RoleRepository roleRepository;

    @Test
    public void testFindById ()
    {
        Role expected = getRole();
        Role actual = roleRepository.findById(1).orElse(null);

        assertEquals(expected, actual);
    }

    @Test
    public void testFindByName ()
    {
        Role expected = getRole();
        Role actual = roleRepository.findByName("USER");

        assertEquals(expected, actual);
    }

    @Test
    public void testFindAll ()
    {
        List<Role> expected = getRoles();
        List<Role> actual = roleRepository.findAll();

        assertEquals(expected, actual);
    }

    @Test
    public void testAddRole ()
    {
        Role role = new Role("MODERATOR", "Moderator");
        role = roleRepository.save(role);

        Role actual = roleRepository.findById(role.getId()).orElse(null);
        Role expected = getRole("MODERATOR", "Moderator", 3);

        assertEquals(expected, actual);
    }

    @Test
    public void testUpdateRole ()
    {
        Role role = roleRepository.findById(2).orElse(null);
        if (role != null)
        {
            role.setName("ADMINUpdated");
            roleRepository.save(role);
        }

        Role actual = roleRepository.findById(2).orElse(null);
        Role expected = getRole("ADMINUpdated", "Admin", 2);

        assertEquals(expected, actual);
    }

    @Test
    public void testDeleteRole ()
    {
        roleRepository.findById(2).ifPresent(role -> roleRepository.delete(role));

        Role actual = roleRepository.findById(2).orElse(null);

        assertNull(actual);
    }

    private Role getRole ()
    {
        Role role = new Role("USER", "User");
        role.setId(1);
        return role;
    }

    private Role getRole (String name, String description, int id)
    {
        Role role = new Role(name, description);
        role.setId(id);
        return role;
    }

    private List<Role> getRoles ()
    {
        return Arrays.asList(getRole(), getRole("ADMIN", "Admin", 2));
    }
}