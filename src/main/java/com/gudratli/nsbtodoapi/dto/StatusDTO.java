package com.gudratli.nsbtodoapi.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class StatusDTO
{
    private Integer id;
    @NotBlank(message = "Name can not be null nor empty, must have at least one character except space")
    private String name;
}
