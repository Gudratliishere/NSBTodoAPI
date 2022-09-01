package com.gudratli.nsbtodoapi.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@Data
@ApiModel(value = "Email Token", description = "DTO that used for store email token when verifying email on register.")
public class EmailTokenDTO
{
    @ApiModelProperty(value = "ID of Email Token, you need this when verifying email.", example = "16")
    private Integer id;

    @ApiModelProperty(value = "Email which created token for that.", example = "example@gmail.com")
    private String email;

    @ApiModelProperty("Status that sign token has used or not.")
    private Boolean status;

    @ApiModelProperty("Date when email token will expire.")
    private Date expireTime;
}
