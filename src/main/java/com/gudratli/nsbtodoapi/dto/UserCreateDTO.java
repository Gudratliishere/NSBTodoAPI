package com.gudratli.nsbtodoapi.dto;

import lombok.Data;

@Data
public class UserCreateDTO
{
    private String name;
    private String surname;
    private String phone;
    private String email;
    private String github;
    private String address;
    private String cv;
    private String username;
    private String password;
    private Integer countryId;
    private Integer roleId;
}
