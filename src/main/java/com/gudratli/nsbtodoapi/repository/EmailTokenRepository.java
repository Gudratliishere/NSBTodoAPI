package com.gudratli.nsbtodoapi.repository;

import com.gudratli.nsbtodoapi.entity.EmailToken;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmailTokenRepository extends JpaRepository<EmailToken, Integer>
{
}