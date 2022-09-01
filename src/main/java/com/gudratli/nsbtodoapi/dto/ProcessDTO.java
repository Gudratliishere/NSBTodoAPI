package com.gudratli.nsbtodoapi.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.Date;

@Data
@ApiModel(value = "Process", description = "Process that admin submitted task to user.")
public class ProcessDTO
{
    @ApiModelProperty(value = "ID of process. You don't need to set id when creating new process.", example = "19")
    private Integer id;

    @NotNull(message = "Process must have user id")
    @ApiModelProperty(value = "User id which task submitted to that user.", example = "17", required = true)
    private Integer userId;

    @NotNull(message = "Process must have task id")
    @ApiModelProperty(value = "Task id which submitted to user.", example = "15", required = true)
    private Integer taskId;

    @ApiModelProperty("Date when task submitted to user.")
    private Date startDate;

    @NotNull(message = "Deadline can not be null nor empty")
    @ApiModelProperty(value = "Date when task time will end.", required = true)
    private Date deadline;

    @NotNull(message = "End date can not be null nor empty")
    @ApiModelProperty("Date when task finished by user.")
    private Date endDate;

    @ApiModelProperty("Id of status which indicates that task has been finished or not.")
    private Integer statusId;
}
