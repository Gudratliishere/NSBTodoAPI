package com.gudratli.nsbtodoapi.dto;

import lombok.Data;

@Data
public class UserTechnologyDTO
{
    private UserDTO userDTO;
    private TechnologyDTO technologyDTO;
    private Integer level;
}
