package com.gudratli.nsbtodoapi.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
@ApiModel(value = "Language", description = "DTO used for storing languages.")
public class LanguageDTO
{
    @ApiModelProperty(value = "You don't need to set ID when creating language.", example = "19")
    private Integer id;

    @NotBlank(message = "Name can not be null nor empty, must have at least one character except space")
    @ApiModelProperty(value = "Name of language", example = "English", required = true)
    private String name;
}
