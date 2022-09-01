package com.gudratli.nsbtodoapi.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
@ApiModel(value = "Task", description = "DTO used for storing tasks given by admins.")
public class TaskDTO
{
    @ApiModelProperty("ID of task, you don't need to set ID when creating new task.")
    private Integer id;

    @NotBlank(message = "Name can not be null nor empty, must have at least one character except space")
    @ApiModelProperty(value = "Name of task.", example = "If Else statements.", required = true)
    private String name;

    @NotBlank(message = "Task description can not be null nor empty, must have at least one character except space")
    @ApiModelProperty(value = "Description about given task.",
            example = "You have to write program that handles with it..", required = true)
    private String description;
}
