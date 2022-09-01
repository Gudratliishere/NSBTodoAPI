package com.gudratli.nsbtodoapi.dto;

import com.gudratli.nsbtodoapi.annotation.validator.StartsWith;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Data
@ApiModel(value = "User", description = "DTO used for storing user information and update user.")
public class UserDTO
{
    @ApiModelProperty(value = "ID of user.", example = "12", required = true)
    private Integer id;

    @NotBlank(message = "Name can not be null nor empty, must have at least one character except space")
    @ApiModelProperty(value = "Name of user.", example = "Dunay", required = true)
    private String name;

    @NotBlank(message = "Surname can not be null nor empty, must have at least one character except space")
    @ApiModelProperty(value = "Surname of user.", example = "Gudratli", required = true)
    private String surname;

    @Pattern(regexp = "-?[0-9]+", message = "Phone must contain only digits")
    @ApiModelProperty(value = "Phone number of user, contains only digits.", example = "0222222222", required = true)
    private String phone;

    @NotBlank(message = "Email can not be null nor empty, must have at least one character except space")
    @Email(message = "Please type valid email address")
    @ApiModelProperty(value = "Email of user.", example = "d.qudretli@gmail.com", required = true)
    private String email;

    @NotBlank(message = "Email can not be null nor empty, must have at least one character except space")
    @StartsWith(value = "https://github.com/", message = "Github link must starts with valid link")
    @ApiModelProperty(value = "Link of github address.", example = "https://github.com/Gudratliishere", required = true)
    private String github;

    @NotBlank(message = "Address can not be null nor empty, must have at least one character except space")
    @ApiModelProperty(value = "Address of user.", example = "Baku, Masazir.", required = true)
    private String address;

    @NotNull(message = "User must have cv")
    @ApiModelProperty(value = "CV ID of user.", example = "15", required = true)
    private Integer cv;

    @NotBlank(message = "Username can not be null nor empty, must have at least one character except space")
    @ApiModelProperty(value = "Username of user, it must be unique.", example = "dunayqudrtli", required = true)
    private String username;

    @NotNull(message = "User must have country id")
    @ApiModelProperty(value = "ID of country of user.", example = "12", required = true)
    private Integer countryId;
}
