package com.gudratli.nsbtodoapi.service.impl;

import com.gudratli.nsbtodoapi.entity.EmailToken;
import com.gudratli.nsbtodoapi.repository.EmailTokenRepository;
import com.gudratli.nsbtodoapi.service.inter.EmailTokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@RequiredArgsConstructor
@Service
public class EmailTokenServiceImpl implements EmailTokenService
{
    private final EmailTokenRepository emailTokenRepository;

    @Override
    public List<EmailToken> getAll ()
    {
        return emailTokenRepository.findAll();
    }

    @Override
    public List<EmailToken> getByEmail (String email)
    {
        return emailTokenRepository.findByEmail(email);
    }

    @Override
    public EmailToken getById (Integer id)
    {
        return emailTokenRepository.findById(id).orElse(null);
    }

    @Override
    public EmailToken getActiveByEmail (String email)
    {
        List<EmailToken> emailTokens = emailTokenRepository.findByEmailAndStatus(email, true);

        return (emailTokens.size() > 0) ? emailTokens.get(0) : null;
    }

    @Override
    public EmailToken add (EmailToken emailToken)
    {
        List<EmailToken> emailTokens = emailTokenRepository.findByEmail(emailToken.getEmail());
        emailTokens.forEach(emailToken1 ->
        {
            emailToken1.setStatus(false);
            emailTokenRepository.save(emailToken1);
        });

        return emailTokenRepository.save(emailToken);
    }

    @Override
    public EmailToken update (EmailToken emailToken)
    {
        return emailTokenRepository.save(emailToken);
    }

    @Override
    public void remove (Integer id)
    {
        emailTokenRepository.findById(id).ifPresent(emailTokenRepository::delete);
    }

    @Override
    public void expireEmailToken (Integer id)
    {
        emailTokenRepository.findById(id).ifPresent(emailToken ->
        {
            emailToken.setStatus(false);
            emailTokenRepository.save(emailToken);
        });
    }

    @Override
    public Boolean isExpired (EmailToken emailToken)
    {
        return !emailToken.getExpireTime().after(new Date());
    }
}
