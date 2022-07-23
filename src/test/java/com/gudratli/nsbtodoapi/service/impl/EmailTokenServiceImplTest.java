package com.gudratli.nsbtodoapi.service.impl;

import com.gudratli.nsbtodoapi.entity.EmailToken;
import com.gudratli.nsbtodoapi.repository.EmailTokenRepository;
import com.gudratli.nsbtodoapi.service.inter.EmailTokenService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static com.gudratli.nsbtodoapi.util.Entities.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class EmailTokenServiceImplTest
{
    private EmailTokenRepository emailTokenRepository;

    private EmailTokenService emailTokenService;

    @BeforeEach
    public void setUp ()
    {
        emailTokenRepository = mock(EmailTokenRepository.class);

        emailTokenService = new EmailTokenServiceImpl(emailTokenRepository);
    }

    @Test
    public void testGetAll ()
    {
        List<EmailToken> expected = getEmailTokenList();
        when(emailTokenRepository.findAll()).thenReturn(expected);

        List<EmailToken> actual = emailTokenService.getAll();

        assertEquals(expected, actual);
        verify(emailTokenRepository).findAll();
    }

    @Test
    public void testGetByEmail ()
    {
        List<EmailToken> expected = getEmailTokenList();
        when(emailTokenRepository.findByEmail(expected.get(0).getEmail())).thenReturn(expected);

        List<EmailToken> actual = emailTokenService.getByEmail(expected.get(0).getEmail());

        assertEquals(expected, actual);
        verify(emailTokenRepository).findByEmail(expected.get(0).getEmail());
    }

    @Test
    public void testGetById ()
    {
        EmailToken expected = getEmailToken();
        when(emailTokenRepository.findById(expected.getId())).thenReturn(Optional.of(expected));

        EmailToken actual = emailTokenService.getById(expected.getId());

        assertEquals(expected, actual);
        verify(emailTokenRepository).findById(expected.getId());
    }

    @Test
    public void testGetActiveByEmail ()
    {
        EmailToken expected = getEmailToken();
        when(emailTokenRepository.findByEmailAndStatus(expected.getEmail(), true)).thenReturn(
                Collections.singletonList(expected));

        EmailToken actual = emailTokenService.getActiveByEmail(expected.getEmail());

        assertEquals(expected, actual);
        verify(emailTokenRepository).findByEmailAndStatus(expected.getEmail(), true);
    }

    @Test
    public void testAdd ()
    {
        EmailToken emailToken = new EmailToken("orxan@gmail", "6564", parse("2022-07-22 15:45:33"));
        EmailToken expected = getEmailToken(emailToken.getEmail(), emailToken.getToken(),
                emailToken.getExpireTime(), 5);
        when(emailTokenRepository.save(emailToken)).thenReturn(expected);

        EmailToken actual = emailTokenService.add(emailToken);

        assertEquals(expected, actual);
        verify(emailTokenRepository).save(emailToken);
    }

    @Test
    public void testUpdate ()
    {
        EmailToken expected = getEmailToken("orxan@gmail", "6564", parse("2022-07-22 15:45:33"), 5);
        when(emailTokenRepository.save(expected)).thenReturn(expected);

        EmailToken actual = emailTokenService.update(expected);

        assertEquals(expected, actual);
        verify(emailTokenRepository).save(expected);
    }

    @Test
    public void testRemove ()
    {
        EmailToken emailToken = getEmailToken();
        when(emailTokenRepository.findById(emailToken.getId())).thenReturn(Optional.of(emailToken));

        emailTokenService.remove(emailToken.getId());

        when(emailTokenRepository.findById(emailToken.getId())).thenReturn(Optional.empty());

        EmailToken actual = emailTokenService.getById(emailToken.getId());

        assertNull(actual);
    }

    @Test
    public void testExpireEmailToken ()
    {
        EmailToken emailToken = getEmailToken();
        when(emailTokenRepository.findById(emailToken.getId())).thenReturn(Optional.of(emailToken));
        emailToken.setStatus(false);
        when(emailTokenRepository.save(emailToken)).thenReturn(emailToken);

        emailTokenService.expireEmailToken(emailToken.getId());

        verify(emailTokenRepository).findById(emailToken.getId());
        verify(emailTokenRepository).save(emailToken);
    }

    @Test
    public void testIsExpired_itShouldReturnTrue ()
    {
        EmailToken emailToken = getEmailToken();
        emailToken.setExpireTime(parse("2022-05-27 09:05:22"));

        Boolean actual = emailTokenService.isExpired(emailToken);

        assertTrue(actual);
    }

    @Test
    public void testIsExpired_itShouldReturnFalse ()
    {
        EmailToken emailToken = getEmailToken();
        emailToken.setExpireTime(parse("2022-05-27 16:05:22"));

        Boolean actual = emailTokenService.isExpired(emailToken);

        assertFalse(actual);
    }
}