package com.gudratli.nsbtodoapi.dto;

import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
public class UserAuthDTO
{
    private Integer id;
    @NotEmpty(message = "Password can not be null nor empty")
    private String password;
}
