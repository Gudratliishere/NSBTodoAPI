package com.gudratli.nsbtodoapi.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
@ApiModel(value = "Email Token Verify", description = "DTO used for verifying email when token sent.")
public class EmailTokenVerifyDTO
{
    @NotNull(message = "Id can not be null")
    @ApiModelProperty(value = "ID of token which generated before.", example = "13", required = true)
    private Integer id;

    @NotEmpty(message = "Token can not be null nor empty")
    @ApiModelProperty(value = "Token which sent to email.", required = true)
    private String token;
}
