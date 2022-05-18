package com.gudratli.nsbtodoapi.service.inter;

import com.gudratli.nsbtodoapi.entity.UserTechnology;
import org.springframework.stereotype.Service;

import java.util.List;

public interface UserTechnologyService
{
    List<UserTechnology> getAll ();

    List<UserTechnology> getByUserId (Integer id);

    List<UserTechnology> getByTechnologyId (Integer id);

    UserTechnology getById (Integer id);

    UserTechnology add (UserTechnology userTechnology);

    UserTechnology update (UserTechnology userTechnology);

    void remove (Integer id);
}
