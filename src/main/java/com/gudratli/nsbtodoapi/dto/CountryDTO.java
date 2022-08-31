package com.gudratli.nsbtodoapi.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class CountryDTO
{
    private Integer id;

    @NotBlank(message = "Name can not be null nor empty, must have at least one character except space")
    private String name;

    @NotNull(message = "Country must have region id.")
    private Integer regionId;
}
