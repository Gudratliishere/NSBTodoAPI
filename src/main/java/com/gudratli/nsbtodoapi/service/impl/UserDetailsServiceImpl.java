package com.gudratli.nsbtodoapi.service.impl;

import com.gudratli.nsbtodoapi.entity.User;
import com.gudratli.nsbtodoapi.service.inter.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

@RequiredArgsConstructor
@Service
public class UserDetailsServiceImpl implements UserDetailsService
{
    private final UserService userService;

    @Override
    public UserDetails loadUserByUsername (String username) throws UsernameNotFoundException
    {
        User user = userService.getByUsername(username);
        if (user == null)
            throw new UsernameNotFoundException("User doesn't exist with this username.");

        SimpleGrantedAuthority simpleGrantedAuthority = new SimpleGrantedAuthority(user.getRole().getName());
        return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(),
                Collections.singleton(simpleGrantedAuthority));
    }
}
