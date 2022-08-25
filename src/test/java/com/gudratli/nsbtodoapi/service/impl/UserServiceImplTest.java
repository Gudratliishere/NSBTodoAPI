package com.gudratli.nsbtodoapi.service.impl;

import com.gudratli.nsbtodoapi.entity.Country;
import com.gudratli.nsbtodoapi.entity.Role;
import com.gudratli.nsbtodoapi.entity.User;
import com.gudratli.nsbtodoapi.exception.duplicate.DuplicateEmailException;
import com.gudratli.nsbtodoapi.exception.duplicate.DuplicatePhoneException;
import com.gudratli.nsbtodoapi.exception.duplicate.DuplicateUsernameException;
import com.gudratli.nsbtodoapi.repository.CountryRepository;
import com.gudratli.nsbtodoapi.repository.RoleRepository;
import com.gudratli.nsbtodoapi.repository.UserRepository;
import com.gudratli.nsbtodoapi.service.inter.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.List;
import java.util.Optional;

import static com.gudratli.nsbtodoapi.util.Entities.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceImplTest
{
    private UserRepository userRepository;
    private CountryRepository countryRepository;
    private RoleRepository roleRepository;

    private BCryptPasswordEncoder bCryptPasswordEncoder;

    private UserService userService;

    @BeforeEach
    public void setUp ()
    {
        userRepository = mock(UserRepository.class);
        countryRepository = mock(CountryRepository.class);
        roleRepository = mock(RoleRepository.class);
        bCryptPasswordEncoder = mock(BCryptPasswordEncoder.class);

        userService = new UserServiceImpl(userRepository, countryRepository, roleRepository, bCryptPasswordEncoder);
    }

    @Test
    public void testGetAll ()
    {
        List<User> expected = getUserList();
        when(userRepository.findAll()).thenReturn(expected);

        List<User> actual = userService.getAll();

        assertEquals(expected, actual);
        verify(userRepository).findAll();
    }

    @Test
    public void testGetByNameContaining ()
    {
        List<User> expected = getUserList();
        when(userRepository.findByNameContaining("ala")).thenReturn(expected);

        List<User> actual = userService.getByNameContaining("ala");

        assertEquals(expected, actual);
        verify(userRepository).findByNameContaining("ala");
    }

    @Test
    public void testGetBySurnameContaining ()
    {
        List<User> expected = getUserList();
        when(userRepository.findBySurnameContaining("ala")).thenReturn(expected);

        List<User> actual = userService.getBySurnameContaining("ala");

        assertEquals(expected, actual);
        verify(userRepository).findBySurnameContaining("ala");
    }

    @Test
    public void testGetByCountryId ()
    {
        List<User> expected = getUserList();
        Country country = getCountry();
        when(userRepository.findByCountry(country)).thenReturn(expected);
        when(countryRepository.findById(country.getId())).thenReturn(Optional.of(country));

        List<User> actual = userService.getByCountryId(country.getId());

        assertEquals(expected, actual);
        verify(userRepository).findByCountry(country);
        verify(countryRepository).findById(country.getId());
    }

    @Test
    public void testGetByRoleId ()
    {
        List<User> expected = getUserList();
        Role role = getRole();
        when(userRepository.findByRole(role)).thenReturn(expected);
        when(roleRepository.findById(role.getId())).thenReturn(Optional.of(role));

        List<User> actual = userService.getByRoleId(role.getId());

        assertEquals(expected, actual);
        verify(userRepository).findByRole(role);
        verify(roleRepository).findById(role.getId());
    }

    @Test
    public void testGetById ()
    {
        User expected = getUser();
        when(userRepository.findById(expected.getId())).thenReturn(Optional.of(expected));

        User actual = userService.getById(expected.getId());

        assertEquals(expected, actual);
        verify(userRepository).findById(expected.getId());
    }

    @Test
    public void testGetByPhone ()
    {
        User expected = getUser();
        when(userRepository.findByPhone(expected.getPhone())).thenReturn(expected);

        User actual = userService.getByPhone(expected.getPhone());

        assertEquals(expected, actual);
        verify(userRepository).findByPhone(expected.getPhone());
    }

    @Test
    public void testGetByEmail ()
    {
        User expected = getUser();
        when(userRepository.findByEmail(expected.getEmail())).thenReturn(expected);

        User actual = userService.getByEmail(expected.getEmail());

        assertEquals(expected, actual);
        verify(userRepository).findByEmail(expected.getEmail());
    }

    @Test
    public void testGetByUsername ()
    {
        User expected = getUser();
        when(userRepository.findByUsername(expected.getUsername())).thenReturn(expected);

        User actual = userService.getByUsername(expected.getUsername());

        assertEquals(expected, actual);
        verify(userRepository).findByUsername(expected.getUsername());
    }

    @Test
    public void testAdd_whenValidUser ()
            throws DuplicateUsernameException, DuplicatePhoneException, DuplicateEmailException
    {
        User user = getUser();
        User expected = getUser("Dunay", true, false, 5);
        when(userRepository.save(user)).thenReturn(expected);
        when(userRepository.findByPhone(user.getPhone())).thenReturn(null);
        when(userRepository.findByEmail(user.getEmail())).thenReturn(null);
        when(userRepository.findByUsername(user.getUsername())).thenReturn(null);
        when(bCryptPasswordEncoder.encode(user.getPassword())).thenReturn(user.getPassword());

        User actual = userService.add(user);

        assertEquals(expected, actual);
        verify(userRepository).save(user);
        verify(userRepository).findByPhone(user.getPhone());
        verify(userRepository).findByEmail(user.getEmail());
        verify(userRepository).findByUsername(user.getUsername());
        verify(bCryptPasswordEncoder).encode(user.getPassword());
    }

    @Test
    public void testAdd_whenDuplicatePhoneUser () throws DuplicateUsernameException, DuplicateEmailException
    {
        User user = getUser();
        User expected = getUser("Dunay", true, false, 5);
        when(userRepository.findByPhone(user.getPhone())).thenReturn(expected);
        when(userRepository.findByEmail(user.getEmail())).thenReturn(null);
        when(userRepository.findByUsername(user.getUsername())).thenReturn(null);

        try
        {
            userService.add(user);
        } catch (DuplicatePhoneException e)
        {
            assertInstanceOf(DuplicatePhoneException.class, e);
        }

        verify(userRepository).findByPhone(user.getPhone());
        verify(userRepository).findByEmail(user.getEmail());
        verify(userRepository).findByUsername(user.getUsername());
    }

    @Test
    public void testAdd_whenDuplicateEmailUser () throws DuplicateUsernameException, DuplicatePhoneException
    {
        User user = getUser();
        User expected = getUser("Dunay", true, false, 5);
        when(userRepository.findByPhone(user.getPhone())).thenReturn(null);
        when(userRepository.findByEmail(user.getEmail())).thenReturn(expected);
        when(userRepository.findByUsername(user.getUsername())).thenReturn(null);

        try
        {
            userService.add(user);
        } catch (DuplicateEmailException e)
        {
            assertInstanceOf(DuplicateEmailException.class, e);
        }

        verify(userRepository).findByPhone(user.getPhone());
        verify(userRepository).findByEmail(user.getEmail());
        verify(userRepository).findByUsername(user.getUsername());
    }

    @Test
    public void testAdd_whenDuplicateUsernameUser () throws DuplicateEmailException, DuplicatePhoneException
    {
        User user = getUser();
        User expected = getUser("Dunay", true, false, 5);
        when(userRepository.findByPhone(user.getPhone())).thenReturn(null);
        when(userRepository.findByEmail(user.getEmail())).thenReturn(null);
        when(userRepository.findByUsername(user.getUsername())).thenReturn(expected);

        try
        {
            userService.add(user);
        } catch (DuplicateUsernameException e)
        {
            assertInstanceOf(DuplicateUsernameException.class, e);
        }

        verify(userRepository).findByPhone(user.getPhone());
        verify(userRepository).findByEmail(user.getEmail());
        verify(userRepository).findByUsername(user.getUsername());
    }

    @Test
    public void testUpdate_whenValidUser ()
            throws DuplicateUsernameException, DuplicatePhoneException, DuplicateEmailException
    {
        User user = getUser("Dunay", true, false, 5);
        User expected = getUser(user.getName(), user.getStatus(), user.getBanned(), user.getId() + 1);
        when(userRepository.save(user)).thenReturn(expected);
        when(userRepository.findByPhone(user.getPhone())).thenReturn(null);
        when(userRepository.findByEmail(user.getEmail())).thenReturn(null);
        when(userRepository.findByUsername(user.getUsername())).thenReturn(null);
        when(bCryptPasswordEncoder.encode(user.getPassword())).thenReturn(user.getPassword());

        User actual = userService.update(user);

        assertEquals(expected, actual);
        verify(userRepository).save(user);
        verify(userRepository).findByPhone(user.getPhone());
        verify(userRepository).findByEmail(user.getEmail());
        verify(userRepository).findByUsername(user.getUsername());
        verify(bCryptPasswordEncoder).encode(user.getPassword());
    }

    @Test
    public void testUpdate_whenDuplicatePhoneUser () throws DuplicateUsernameException, DuplicateEmailException
    {
        User user = getUser("Dunay", true, false, 5);
        User expected = getUser(user.getName(), user.getStatus(), user.getBanned(), user.getId() + 1);
        when(userRepository.findByPhone(user.getPhone())).thenReturn(expected);
        when(userRepository.findByEmail(user.getEmail())).thenReturn(null);
        when(userRepository.findByUsername(user.getUsername())).thenReturn(null);

        try
        {
            userService.update(user);
        } catch (DuplicatePhoneException e)
        {
            assertInstanceOf(DuplicatePhoneException.class, e);
        }

        verify(userRepository).findByPhone(user.getPhone());
        verify(userRepository).findByEmail(user.getEmail());
        verify(userRepository).findByUsername(user.getUsername());
    }

    @Test
    public void testUpdate_whenDuplicateEmailUser () throws DuplicateUsernameException, DuplicatePhoneException
    {
        User user = getUser("Dunay", true, false, 5);
        User expected = getUser(user.getName(), user.getStatus(), user.getBanned(), user.getId() + 1);
        when(userRepository.findByPhone(user.getPhone())).thenReturn(null);
        when(userRepository.findByEmail(user.getEmail())).thenReturn(expected);
        when(userRepository.findByUsername(user.getUsername())).thenReturn(null);

        try
        {
            userService.update(user);
        } catch (DuplicateEmailException e)
        {
            assertInstanceOf(DuplicateEmailException.class, e);
        }

        verify(userRepository).findByPhone(user.getPhone());
        verify(userRepository).findByEmail(user.getEmail());
        verify(userRepository).findByUsername(user.getUsername());
    }

    @Test
    public void testUpdate_whenDuplicateUsernameUser () throws DuplicateEmailException, DuplicatePhoneException
    {
        User user = getUser("Dunay", true, false, 5);
        User expected = getUser(user.getName(), user.getStatus(), user.getBanned(), user.getId() + 1);
        when(userRepository.findByPhone(user.getPhone())).thenReturn(null);
        when(userRepository.findByEmail(user.getEmail())).thenReturn(null);
        when(userRepository.findByUsername(user.getUsername())).thenReturn(expected);

        try
        {
            userService.update(user);
        } catch (DuplicateUsernameException e)
        {
            assertInstanceOf(DuplicateUsernameException.class, e);
        }

        verify(userRepository).findByPhone(user.getPhone());
        verify(userRepository).findByEmail(user.getEmail());
        verify(userRepository).findByUsername(user.getUsername());
    }

    @Test
    public void testRemove ()
    {
        User user = getUser();
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));

        userService.remove(user.getId());

        when(userRepository.findById(user.getId())).thenReturn(Optional.empty());

        User actual = userService.getById(user.getId());

        assertNull(actual);
    }
}