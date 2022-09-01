package com.gudratli.nsbtodoapi.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Data
@ApiModel(value = "User technology", description = "DTO used for storing which user have which technology.")
public class UserTechnologyDTO
{
    @ApiModelProperty("ID of user technology, you don't need to set ID when creating new user technology.")
    private Integer id;

    @NotNull(message = "UserTechnology must have user id")
    @ApiModelProperty(value = "ID of user.", example = "14", required = true)
    private Integer userId;

    @NotNull(message = "UserTechnology must have technology id")
    @ApiModelProperty(value = "ID of technology.", example = "12", required = true)
    private Integer technologyId;

    @NotNull(message = "UserLanguage must have level")
    @Min(value = 0, message = "UserLanguage must have at least level 0")
    @Max(value = 10, message = "UserLanguage must have at most level 10")
    @ApiModelProperty(value = "Level of user how much know this technology between 0-10.", example = "5", required = true)
    private Integer level;
}
