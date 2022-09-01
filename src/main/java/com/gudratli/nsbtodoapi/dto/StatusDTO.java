package com.gudratli.nsbtodoapi.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
@ApiModel(value = "Status", description = "DTO used for storing status which indicates task results.")
public class StatusDTO
{
    @ApiModelProperty("ID of status, you don't need to set ID when creating new status type.")
    private Integer id;

    @NotBlank(message = "Name can not be null nor empty, must have at least one character except space")
    @ApiModelProperty(value = "Name of status type.", example = "Completed", required = true)
    private String name;
}
