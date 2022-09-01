package com.gudratli.nsbtodoapi.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "File", description = "DTO used for show information about uploaded files.")
public class FileDTO
{
    @ApiModelProperty("Name of file which saved to server.")
    private String name;

    @ApiModelProperty("Type of file.")
    private String type;

    @ApiModelProperty("Size of file.")
    private double size;

    @ApiModelProperty("Download URL of file, you can use this url to download that file.")
    private String downloadURL;
}
