package com.gudratli.nsbtodoapi.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
@ApiModel(value = "Technology", description = "DTO used for storing technologies.")
public class TechnologyDTO
{
    @ApiModelProperty("ID of technology, you don't need to set ID when creating new technology.")
    private Integer id;

    @NotBlank(message = "Name can not be null nor empty, must have at least one character except space")
    @ApiModelProperty(value = "Name of technology.", example = "Spring framework.", required = true)
    private String name;
}
