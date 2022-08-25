package com.gudratli.nsbtodoapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;

@SpringBootApplication
@EnableMethodSecurity
public class NsbTodoApiApplication
{

    public static void main (String[] args)
    {
        SpringApplication.run(NsbTodoApiApplication.class, args);
    }
}
