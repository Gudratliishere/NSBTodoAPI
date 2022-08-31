package com.gudratli.nsbtodoapi.dto;

import com.gudratli.nsbtodoapi.annotation.validator.StartsWith;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Data
public class UserDTO
{
    private Integer id;

    @NotBlank(message = "Name can not be null nor empty, must have at least one character except space")
    private String name;
    @NotBlank(message = "Surname can not be null nor empty, must have at least one character except space")
    private String surname;
    @Pattern(regexp = "-?[0-9]+", message = "Phone must contain only digits")
    private String phone;
    @NotBlank(message = "Email can not be null nor empty, must have at least one character except space")
    @Email(message = "Please type valid email address")
    private String email;
    @NotBlank(message = "Email can not be null nor empty, must have at least one character except space")
    @StartsWith(value = "https://github.com/", message = "Github link must starts with valid link")
    private String github;
    @NotBlank(message = "Address can not be null nor empty, must have at least one character except space")
    private String address;
    @NotNull(message = "User must have cv")
    private Integer cv;
    @NotBlank(message = "Username can not be null nor empty, must have at least one character except space")
    private String username;
    @NotNull(message = "User must have country id")
    private Integer countryId;
}
