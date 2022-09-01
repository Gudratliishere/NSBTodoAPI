package com.gudratli.nsbtodoapi.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
@ApiModel(value = "Role", description = "DTO used to store roles between users.")
public class RoleDTO
{
    @ApiModelProperty("ID of role, you don't need to set ID when create new role.")
    private Integer id;

    @NotBlank(message = "Name can not be null nor empty, must have at least one character except space")
    @ApiModelProperty(value = "Name of role", example = "ADMIN", required = true)
    private String name;

    @NotBlank(message = "Role description can not be null nor empty, must have at least one character except space")
    @ApiModelProperty(value = "Description about role", example = "This role can access everything.", required = true)
    private String roleDescription;
}
