package com.gudratli.nsbtodoapi.dto;

import lombok.Data;

import java.util.Date;

@Data
public class EmailTokenDTO
{
    private String email;
    private Boolean status;
    private Date expireTime;
}
