package com.gudratli.nsbtodoapi.dto;

import lombok.Data;

@Data
public class FileDTO
{
    private String name;
    private String type;
    private double size;
    private String downloadURL;
}
