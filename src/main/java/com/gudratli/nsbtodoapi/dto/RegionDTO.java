package com.gudratli.nsbtodoapi.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
@ApiModel(value = "Region", description = "DTO used for store regions.")
public class RegionDTO
{
    @ApiModelProperty("ID of region, you don't need to set ID when creating new region.")
    private Integer id;

    @NotBlank(message = "Name can not be null nor empty, must have at least one character except space")
    @ApiModelProperty(value = "Name of region, it must be unique.", example = "Africa", required = true)
    private String name;
}
