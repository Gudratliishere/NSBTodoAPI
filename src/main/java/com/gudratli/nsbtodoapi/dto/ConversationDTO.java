package com.gudratli.nsbtodoapi.dto;

import lombok.Data;

import java.util.Date;

@Data
public class ConversationDTO
{
    private Integer id;
    private Integer userId;
    private Integer processId;
    private String message;
    private Date sendDate;
}
