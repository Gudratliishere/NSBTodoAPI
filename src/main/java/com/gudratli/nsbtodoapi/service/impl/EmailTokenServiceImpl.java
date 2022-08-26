package com.gudratli.nsbtodoapi.service.impl;

import com.gudratli.nsbtodoapi.entity.EmailToken;
import com.gudratli.nsbtodoapi.repository.EmailTokenRepository;
import com.gudratli.nsbtodoapi.service.inter.EmailTokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;

@RequiredArgsConstructor
@Service
public class EmailTokenServiceImpl implements EmailTokenService
{
    private final EmailTokenRepository emailTokenRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

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
    public EmailToken generateToken (String email)
    {
        EmailToken emailToken = new EmailToken(email, getRandomString(),
                new Date(Calendar.getInstance().getTimeInMillis() + 5 * 60 * 1000));
        emailToken.setStatus(true);

        EmailToken activeToken = getActiveByEmail(email);
        if (activeToken != null)
        {
            activeToken.setStatus(false);
            emailTokenRepository.save(activeToken);
        }

        emailToken = emailTokenRepository.save(emailToken);
        return emailToken;
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

    private String getRandomString ()
    {
        String chars = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijk"
                + "lmnopqrstuvwxyz!@#$%&";
        int len = 10;
        Random rnd = new Random();
        StringBuilder sb = new StringBuilder(len);
        for (int i = 0; i < len; i++)
            sb.append(chars.charAt(rnd.nextInt(chars.length())));
        return bCryptPasswordEncoder.encode(sb);
    }
}
