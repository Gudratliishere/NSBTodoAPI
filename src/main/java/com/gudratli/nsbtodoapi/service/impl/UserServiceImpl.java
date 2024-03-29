package com.gudratli.nsbtodoapi.service.impl;

import com.gudratli.nsbtodoapi.entity.Role;
import com.gudratli.nsbtodoapi.entity.User;
import com.gudratli.nsbtodoapi.exception.duplicate.DuplicateEmailException;
import com.gudratli.nsbtodoapi.exception.duplicate.DuplicatePhoneException;
import com.gudratli.nsbtodoapi.exception.duplicate.DuplicateUsernameException;
import com.gudratli.nsbtodoapi.repository.CountryRepository;
import com.gudratli.nsbtodoapi.repository.RoleRepository;
import com.gudratli.nsbtodoapi.repository.UserRepository;
import com.gudratli.nsbtodoapi.service.inter.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService
{
    private final UserRepository userRepository;
    private final CountryRepository countryRepository;
    private final RoleRepository roleRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    @Override
    public List<User> getAll ()
    {
        return userRepository.findAll();
    }

    @Override
    public List<User> getByNameContaining (String name)
    {
        return userRepository.findByNameContaining(name);
    }

    @Override
    public List<User> getBySurnameContaining (String surname)
    {
        return userRepository.findBySurnameContaining(surname);
    }

    @Override
    public List<User> getByCountryId (Integer id)
    {
        return userRepository.findByCountry(countryRepository.findById(id).orElse(null));
    }

    @Override
    public List<User> getByRoleId (Integer id)
    {
        return userRepository.findByRole(roleRepository.findById(id).orElse(null));
    }

    @Override
    public User getById (Integer id)
    {
        return userRepository.findById(id).orElse(null);
    }

    @Override
    public User getByPhone (String phone)
    {
        return userRepository.findByPhone(phone);
    }

    @Override
    public User getByEmail (String email)
    {
        return userRepository.findByEmail(email);
    }

    @Override
    public User getByUsername (String username)
    {
        return userRepository.findByUsername(username);
    }

    @Override
    public User add (User user) throws DuplicatePhoneException, DuplicateEmailException, DuplicateUsernameException
    {
        checkForDuplicates(user);

        user.setPassword(passwordEncoder.encode(user.getPassword()));

        return userRepository.save(user);
    }

    @Override
    public User update (User user) throws DuplicatePhoneException, DuplicateEmailException, DuplicateUsernameException
    {
        checkForDuplicates(user);

        user.setPassword(passwordEncoder.encode(user.getPassword()));

        return userRepository.save(user);
    }

    @Override
    public User changePassword (Integer id, String password)
    {
        return userRepository.changePassword(id, password);
    }

    @Override
    public User changeStatus (Integer id, Boolean status)
    {
        return userRepository.changeStatus(id, status);
    }

    @Override
    public User changeBanned (Integer id, Boolean banned)
    {
        return userRepository.changeBanned(id, banned);
    }

    @Override
    public User changeRole (Integer id, Integer roleId)
    {
        Role role = roleRepository.findById(roleId).orElse(null);
        if (role != null)
            return userRepository.changeRole(id, role);

        return null;
    }

    @Override
    public void remove (Integer id)
    {
        userRepository.findById(id).ifPresent(userRepository::delete);
    }

    private void checkForDuplicates (User user)
            throws DuplicatePhoneException, DuplicateEmailException, DuplicateUsernameException
    {
        User byPhone = getByPhone(user.getPhone());
        User byEmail = getByEmail(user.getEmail());
        User byUsername = getByUsername(user.getUsername());

        if (byPhone != null && !byPhone.getId().equals(user.getId()))
            throw new DuplicatePhoneException();
        if (byEmail != null && !byEmail.getId().equals(user.getId()))
            throw new DuplicateEmailException();
        if (byUsername != null && !byUsername.getId().equals(user.getId()))
            throw new DuplicateUsernameException();
    }
}
