package com.gudratli.nsbtodoapi.repository;

import com.gudratli.nsbtodoapi.NsbTodoApiApplication;
import com.gudratli.nsbtodoapi.entity.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.transaction.Transactional;
import java.util.List;

import static com.gudratli.nsbtodoapi.util.Entities.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

@ExtendWith(SpringExtension.class)
@Transactional
@SpringBootTest(classes = NsbTodoApiApplication.class)
class UserRepositoryTest
{
    @Autowired
    private UserRepository userRepository;

    @Test
    public void testFindByNameContaining ()
    {
        List<User> expected = getUserList();
        List<User> actual = userRepository.findByNameContaining("una");

        assertEquals(expected, actual);
    }

    @Test
    public void testFindBySurnameContaining ()
    {
        List<User> expected = getUserList();
        List<User> actual = userRepository.findBySurnameContaining("udr");

        assertEquals(expected, actual);
    }

    @Test
    public void testFindByStatus ()
    {
        List<User> expected = getActiveUserList();
        List<User> actual = userRepository.findByStatus(true);

        assertEquals(expected, actual);
    }

    @Test
    public void testFindByBanned ()
    {
        List<User> expected = getBannedUserList();
        List<User> actual = userRepository.findByBanned(true);

        assertEquals(expected, actual);
    }

    @Test
    public void testFindByCountry ()
    {
        List<User> expected = getUserList();
        List<User> actual = userRepository.findByCountry(getCountry());

        assertEquals(expected, actual);
    }

    @Test
    public void testFindByRole ()
    {
        List<User> expected = getUserList();
        List<User> actual = userRepository.findByRole(getRole());

        assertEquals(expected, actual);
    }

    @Test
    public void testFindByPhone ()
    {
        User expected = getActiveUser(6);
        User actual = userRepository.findByPhone("0556105884");

        assertEquals(expected, actual);
    }

    @Test
    public void testFindByEmail ()
    {
        User expected = getActiveUser(6);
        User actual = userRepository.findByEmail("dunay@gmail");

        assertEquals(expected, actual);
    }

    @Test
    public void testFindById ()
    {
        User expected = getActiveUser(6);
        User actual = userRepository.findById(6).orElse(null);

        assertEquals(expected, actual);
    }

    @Test
    public void testAddUser ()
    {
        User user = getUser();
        user = userRepository.save(user);

        User expected = getUser("Turqay", true, true, 9);
        User actual = userRepository.findById(user.getId()).orElse(null);

        assertEquals(expected, actual);
    }

    @Test
    public void testUpdateUser ()
    {
        User user = userRepository.findById(7).orElse(null);
        if (user != null)
        {
            user.setName("Dunay3");
            userRepository.save(user);
        }

        User expected = getUser("Dunay3", false, true, 7);
        User actual = userRepository.findById(7).orElse(null);

        assertEquals(expected, actual);
    }

    @Test
    public void testDeleteUser ()
    {
        userRepository.findById(8).ifPresent(user -> userRepository.delete(user));

        User user = userRepository.findById(8).orElse(null);

        assertNull(user);
    }
}