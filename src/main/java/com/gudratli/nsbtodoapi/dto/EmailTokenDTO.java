package com.gudratli.nsbtodoapi.dto;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import java.util.Date;

@Data
public class EmailTokenDTO
{
    private Integer id;

    @NotEmpty(message = "Email must not be empty nor null")
    @Email(message = "Type valid email address")
    private String email;
    private Boolean status;
    private Date expireTime;
}
