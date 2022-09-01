package com.gudratli.nsbtodoapi.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
@ApiModel(value = "User Auth", description = "DTO used for changing password.")
public class UserAuthDTO
{
    @ApiModelProperty(value = "ID of user which will be changed password.", example = "16", required = true)
    private Integer id;

    @NotEmpty(message = "Password can not be null nor empty")
    @ApiModelProperty(value = "Password that will be verified or new password.", required = true)
    private String password;
}
