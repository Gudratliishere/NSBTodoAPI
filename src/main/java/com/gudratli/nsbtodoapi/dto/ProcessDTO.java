package com.gudratli.nsbtodoapi.dto;

import lombok.Data;

import java.util.Date;

@Data
public class ProcessDTO
{
    private Integer id;
    private Integer userId;
    private Integer taskId;
    private Date startDate;
    private Date deadline;
    private Date endDate;
    private Integer statusId;
}
