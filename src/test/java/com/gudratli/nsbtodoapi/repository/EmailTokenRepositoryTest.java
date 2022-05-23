package com.gudratli.nsbtodoapi.repository;

import com.gudratli.nsbtodoapi.NsbTodoApiApplication;
import com.gudratli.nsbtodoapi.entity.EmailToken;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.transaction.Transactional;
import java.util.Date;

import static com.gudratli.nsbtodoapi.util.Entities.getEmailToken;
import static org.assertj.core.util.DateUtil.parse;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

@ExtendWith(SpringExtension.class)
@Transactional
@SpringBootTest(classes = NsbTodoApiApplication.class)
class EmailTokenRepositoryTest
{
    @Autowired
    private EmailTokenRepository emailTokenRepository;

    @Test
    public void testFindById ()
    {
        EmailToken expected = getEmailToken();
        EmailToken actual = emailTokenRepository.findById(2).orElse(null);

        assertEquals(expected, actual);
    }

    @Test
    public void testFindByEmail ()
    {
        EmailToken expected = getEmailToken();
        EmailToken actual = emailTokenRepository.findByEmail("dunay@gmail").get(0);

        assertEquals(expected, actual);
    }

    @Test
    public void testFindByEmailAndStatus ()
    {
        EmailToken expected = getEmailToken();
        EmailToken actual = emailTokenRepository.findByEmailAndStatus("dunay@gmail", true).get(0);

        assertEquals(expected, actual);
    }

    @Test
    public void testFindByEmailAndStatusAndToken ()
    {
        EmailToken expected = getEmailToken();
        EmailToken actual = emailTokenRepository.findByEmailAndStatusAndToken("dunay@gmail", true, "123");

        assertEquals(expected, actual);
    }

    @Test
    public void testAddEmailToken ()
    {
        Date date = new Date();
        EmailToken emailToken = new EmailToken("dunay3@gmail", "1234", date);
        emailToken.setStatus(true);
        emailToken = emailTokenRepository.save(emailToken);

        EmailToken expected = getEmailToken("dunay3@gmail", "1234", date, 10);
        EmailToken actual = emailTokenRepository.findById(emailToken.getId()).orElse(null);

        assertEquals(expected, actual);
    }

    @Test
    public void testUpdateEmailToken ()
    {
        EmailToken emailToken = emailTokenRepository.findById(3).orElse(null);
        if (emailToken != null)
        {
            emailToken.setEmail("dunay2@gmailUpdated");
            emailTokenRepository.save(emailToken);
        }

        EmailToken expected = getEmailToken("dunay2@gmailUpdated", "124", parse("2012-09-12 00:00:00"), 3);
        EmailToken actual = emailTokenRepository.findById(3).orElse(null);

        assertEquals(expected, actual);
    }

    @Test
    public void testDeleteEmailToken ()
    {
        emailTokenRepository.findById(3).ifPresent(emailToken -> emailTokenRepository.delete(emailToken));

        EmailToken actual = emailTokenRepository.findById(3).orElse(null);

        assertNull(actual);
    }
}