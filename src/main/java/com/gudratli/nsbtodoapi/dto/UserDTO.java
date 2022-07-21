package com.gudratli.nsbtodoapi.dto;

import lombok.Data;

@Data
public class UserDTO
{
    private String name;
    private String surname;
    private String phone;
    private String email;
    private String github;
    private String address;
    private String cv;
    private String username;
    private CountryDTO countryDTO;
    private RoleDTO roleDTO;
}
