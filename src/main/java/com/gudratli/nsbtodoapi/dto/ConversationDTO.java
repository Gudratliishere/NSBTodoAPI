package com.gudratli.nsbtodoapi.dto;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Data
public class ConversationDTO
{
    private Integer id;

    @NotNull(message = "Conversation must have valid user id.")
    private Integer userId;
    @NotNull(message = "Conversation must have valid process id.")
    private Integer processId;
    @NotEmpty(message = "Message should have at least one character.")
    private String message;
    private Date sendDate;
}
