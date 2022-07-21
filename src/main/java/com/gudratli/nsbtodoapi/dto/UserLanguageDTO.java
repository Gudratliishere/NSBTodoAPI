package com.gudratli.nsbtodoapi.dto;

import lombok.Data;

@Data
public class UserLanguageDTO
{
    private UserDTO userDTO;
    private LanguageDTO languageDTO;
    private Integer level;
}
