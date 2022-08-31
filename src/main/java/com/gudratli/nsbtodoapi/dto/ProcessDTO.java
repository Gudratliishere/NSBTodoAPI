package com.gudratli.nsbtodoapi.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.Date;

@Data
public class ProcessDTO
{
    private Integer id;
    @NotNull(message = "Process must have user id")
    private Integer userId;
    @NotNull(message = "Process must have task id")
    private Integer taskId;
    private Date startDate;
    @NotNull(message = "Deadline can not be null nor empty")
    private Date deadline;
    @NotNull(message = "End date can not be null nor empty")
    private Date endDate;
    private Integer statusId;
}
