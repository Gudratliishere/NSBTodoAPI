package com.gudratli.nsbtodoapi.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class TaskDTO
{
    private Integer id;
    @NotBlank(message = "Name can not be null nor empty, must have at least one character except space")
    private String name;
    @NotBlank(message = "Task description can not be null nor empty, must have at least one character except space")
    private String description;
}
