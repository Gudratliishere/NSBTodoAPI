package com.gudratli.nsbtodoapi.dto;

import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Data
public class UserLanguageDTO
{
    private Integer id;
    @NotNull(message = "UserLanguage must have user id")
    private Integer userId;
    @NotNull(message = "UserLanguage must have language id")
    private Integer languageId;
    @NotNull(message = "UserLanguage must have level")
    @Min(value = 0, message = "UserLanguage must have at least level 0")
    @Max(value = 10, message = "UserLanguage must have at most level 10")
    private Integer level;
}
