package com.gudratli.nsbtodoapi.dto;

import lombok.Data;

@Data
public class UserLanguageDTO
{
    private Integer id;
    private Integer userId;
    private Integer languageId;
    private Integer level;
}
