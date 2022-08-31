package com.gudratli.nsbtodoapi.dto;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
public class EmailTokenVerifyDTO
{
    @NotNull(message = "Id can not be null")
    private Integer id;
    @NotEmpty(message = "Token can not be null nor empty")
    private String token;
}
