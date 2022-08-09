package com.gudratli.nsbtodoapi.service.inter;

import com.gudratli.nsbtodoapi.entity.EmailToken;
import org.springframework.stereotype.Service;

import java.util.List;

public interface EmailTokenService
{
    List<EmailToken> getAll ();

    List<EmailToken> getByEmail (String email);

    EmailToken getById (Integer id);

    EmailToken getActiveByEmail (String email);

    EmailToken generateToken (String email);

    void expireEmailToken (Integer id);

    Boolean isExpired (EmailToken emailToken);
}
