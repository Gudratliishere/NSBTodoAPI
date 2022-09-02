package com.gudratli.nsbtodoapi.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@ApiModel(value = "Country", description = "DTO that used for store data about countries.")
public class CountryDTO
{
    @ApiModelProperty(value = "You don't need to set id when creating new country.", example = "6")
    private Integer id;

    @NotBlank(message = "Name can not be null nor empty, must have at least one character except space")
    @ApiModelProperty(value = "Name of country.", example = "Egypt", required = true)
    private String name;

    @NotNull(message = "Country must have region id.")
    @ApiModelProperty(value = "Id of region which country is in.", example = "3", required = true)
    private Integer regionId;
}
