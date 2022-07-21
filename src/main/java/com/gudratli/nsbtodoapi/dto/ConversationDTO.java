package com.gudratli.nsbtodoapi.dto;

import lombok.Data;

import java.util.Date;

@Data
public class ConversationDTO
{
    private UserDTO userDTO;
    private ProcessDTO processDTO;
    private String message;
    private Date sendDate;
}
