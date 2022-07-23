package com.gudratli.nsbtodoapi.dto;

import lombok.Data;

@Data
public class TaskDTO
{
    private Integer id;
    private String name;
    private String description;
    private String documentation;
    private String result;
}
