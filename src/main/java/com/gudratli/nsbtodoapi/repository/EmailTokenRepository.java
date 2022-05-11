package com.gudratli.nsbtodoapi.repository;

import com.gudratli.nsbtodoapi.entity.EmailToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EmailTokenRepository extends JpaRepository<EmailToken, Integer>
{
    List<EmailToken> findByEmail (String email);
    List<EmailToken> findByEmailAndStatus (String email, Boolean status);
    EmailToken findByEmailAndStatusAndToken (String email, Boolean status, String token);
}