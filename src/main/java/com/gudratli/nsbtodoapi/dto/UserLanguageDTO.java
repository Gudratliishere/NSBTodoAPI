package com.gudratli.nsbtodoapi.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Data
@ApiModel(value = "User language", description = "DTO used to store which user have which language.")
public class UserLanguageDTO
{
    @ApiModelProperty("ID of user language, you don't need to set ID when creating new user language.")
    private Integer id;

    @NotNull(message = "UserLanguage must have user id")
    @ApiModelProperty(value = "ID of user.", example = "15", required = true)
    private Integer userId;

    @NotNull(message = "UserLanguage must have language id")
    @ApiModelProperty(value = "Id of language.", example = "12", required = true)
    private Integer languageId;

    @NotNull(message = "UserLanguage must have level")
    @Min(value = 0, message = "UserLanguage must have at least level 0")
    @Max(value = 10, message = "UserLanguage must have at most level 10")
    @ApiModelProperty(value = "Level of user how much know this language between 0-10.", example = "5", required = true)
    private Integer level;
}
