package com.gudratli.nsbtodoapi.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Data
@ApiModel(value = "Conversation",
        description = "This DTO is used for store messages between admin and user under task processes.")
public class ConversationDTO
{
    @ApiModelProperty(value = "You don't need to set id when creating conversation", example = "16")
    private Integer id;

    @NotNull(message = "Conversation must have valid user id.")
    @ApiModelProperty(value = "Id of user whom sends message.", example = "6", required = true)
    private Integer userId;

    @NotNull(message = "Conversation must have valid process id.")
    @ApiModelProperty(value = "Id of process which user sends message under it.", example = "4",
            required = true)
    private Integer processId;

    @NotEmpty(message = "Message should have at least one character.")
    @ApiModelProperty(value = "Message that sent under process.",
            example = "When should I submit result?", required = true)
    private String message;

    @ApiModelProperty(value = "Date when message sent.")
    private Date sendDate;
}
