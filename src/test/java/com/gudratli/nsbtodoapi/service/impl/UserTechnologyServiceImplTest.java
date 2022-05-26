package com.gudratli.nsbtodoapi.service.impl;

import com.gudratli.nsbtodoapi.entity.Technology;
import com.gudratli.nsbtodoapi.entity.User;
import com.gudratli.nsbtodoapi.entity.UserTechnology;
import com.gudratli.nsbtodoapi.exception.duplicate.DuplicateUserTechnologyException;
import com.gudratli.nsbtodoapi.repository.TechnologyRepository;
import com.gudratli.nsbtodoapi.repository.UserRepository;
import com.gudratli.nsbtodoapi.repository.UserTechnologyRepository;
import com.gudratli.nsbtodoapi.service.inter.UserTechnologyService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static com.gudratli.nsbtodoapi.util.Entities.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserTechnologyServiceImplTest
{
    private UserTechnologyRepository userTechnologyRepository;
    private UserRepository userRepository;
    private TechnologyRepository technologyRepository;

    private UserTechnologyService userTechnologyService;

    @BeforeEach
    public void setUp ()
    {
        userTechnologyRepository = mock(UserTechnologyRepository.class);
        userRepository = mock(UserRepository.class);
        technologyRepository = mock(TechnologyRepository.class);

        userTechnologyService = new UserTechnologyServiceImpl(userTechnologyRepository, userRepository,
                technologyRepository);
    }

    @Test
    public void testGetAll ()
    {
        List<UserTechnology> expected = getUserTechnologyList();
        when(userTechnologyRepository.findAll()).thenReturn(expected);

        List<UserTechnology> actual = userTechnologyService.getAll();

        assertEquals(expected, actual);
        verify(userTechnologyRepository).findAll();
    }

    @Test
    public void testGetByUserId ()
    {
        List<UserTechnology> expected = getUserTechnologyList();
        User user = getUser();
        when(userTechnologyRepository.findByUser(user)).thenReturn(expected);
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));

        List<UserTechnology> actual = userTechnologyService.getByUserId(user.getId());

        assertEquals(expected, actual);
        verify(userTechnologyRepository).findByUser(user);
        verify(userRepository).findById(user.getId());
    }

    @Test
    public void testGetByTechnologyId ()
    {
        List<UserTechnology> expected = getUserTechnologyList();
        Technology technology = getTechnology();
        when(userTechnologyRepository.findByTechnology(technology)).thenReturn(expected);
        when(technologyRepository.findById(technology.getId())).thenReturn(Optional.of(technology));

        List<UserTechnology> actual = userTechnologyService.getByTechnologyId(technology.getId());

        assertEquals(expected, actual);
        verify(userTechnologyRepository).findByTechnology(technology);
        verify(technologyRepository).findById(technology.getId());
    }

    @Test
    public void testGetById ()
    {
        UserTechnology expected = getUserTechnology();
        when(userTechnologyRepository.findById(expected.getId())).thenReturn(Optional.of(expected));

        UserTechnology actual = userTechnologyService.getById(expected.getId());

        assertEquals(expected, actual);
        verify(userTechnologyRepository).findById(expected.getId());
    }

    @Test
    public void testAdd_whenValidUserTechnology () throws DuplicateUserTechnologyException
    {
        UserTechnology userTechnology = new UserTechnology(getUser(), getTechnology(), 9);
        UserTechnology expected = getUserTechnology(userTechnology.getLevel(), 5);
        when(userTechnologyRepository.save(userTechnology)).thenReturn(expected);
        when(userTechnologyRepository.findByUserAndTechnology(userTechnology.getUser(),
                userTechnology.getTechnology())).thenReturn(null);

        UserTechnology actual = userTechnologyService.add(userTechnology);

        assertEquals(expected, actual);
        verify(userTechnologyRepository).save(userTechnology);
        verify(userTechnologyRepository).findByUserAndTechnology(userTechnology.getUser(),
                userTechnology.getTechnology());
    }

    @Test
    public void testAdd_whenDuplicateUserTechnology ()
    {
        UserTechnology userTechnology = new UserTechnology(getUser(), getTechnology(), 9);
        UserTechnology expected = getUserTechnology(userTechnology.getLevel(), 5);
        when(userTechnologyRepository.findByUserAndTechnology(userTechnology.getUser(),
                userTechnology.getTechnology())).thenReturn(expected);

        try
        {
            userTechnologyService.add(userTechnology);
        } catch (DuplicateUserTechnologyException e)
        {
            assertInstanceOf(DuplicateUserTechnologyException.class, e);
        }

        verify(userTechnologyRepository).findByUserAndTechnology(userTechnology.getUser(),
                userTechnology.getTechnology());
    }

    @Test
    public void testUpdate_whenValidUserTechnology () throws DuplicateUserTechnologyException
    {
        UserTechnology userTechnology = getUserTechnology(5, 4);
        UserTechnology expected = getUserTechnology(userTechnology.getLevel(), userTechnology.getId() + 1);
        when(userTechnologyRepository.save(userTechnology)).thenReturn(expected);
        when(userTechnologyRepository.findByUserAndTechnology(userTechnology.getUser(),
                userTechnology.getTechnology())).thenReturn(null);

        UserTechnology actual = userTechnologyService.update(userTechnology);

        assertEquals(expected, actual);
        verify(userTechnologyRepository).save(userTechnology);
        verify(userTechnologyRepository).findByUserAndTechnology(userTechnology.getUser(),
                userTechnology.getTechnology());
    }

    @Test
    public void testUpdate_whenDuplicateUserTechnology ()
    {
        UserTechnology userTechnology = getUserTechnology(5, 4);
        UserTechnology expected = getUserTechnology(userTechnology.getLevel(), userTechnology.getId() + 1);
        when(userTechnologyRepository.findByUserAndTechnology(userTechnology.getUser(),
                userTechnology.getTechnology())).thenReturn(expected);

        try
        {
            userTechnologyService.update(userTechnology);
        } catch (DuplicateUserTechnologyException e)
        {
            assertInstanceOf(DuplicateUserTechnologyException.class, e);
        }

        verify(userTechnologyRepository).findByUserAndTechnology(userTechnology.getUser(),
                userTechnology.getTechnology());
    }

    @Test
    public void testRemove ()
    {
        UserTechnology userTechnology = getUserTechnology();
        when(userTechnologyRepository.findById(userTechnology.getId())).thenReturn(Optional.of(userTechnology));

        userTechnologyService.remove(userTechnology.getId());

        when(userTechnologyRepository.findById(userTechnology.getId())).thenReturn(Optional.empty());

        UserTechnology actual = userTechnologyService.getById(userTechnology.getId());

        assertNull(actual);
    }
}