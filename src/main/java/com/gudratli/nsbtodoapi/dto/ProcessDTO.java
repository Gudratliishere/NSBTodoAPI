package com.gudratli.nsbtodoapi.dto;

import lombok.Data;

import java.util.Date;

@Data
public class ProcessDTO
{
    private UserDTO userDTO;
    private TaskDTO taskDTO;
    private Date startDate;
    private Date deadline;
    private Date endDate;
    private StatusDTO statusDTO;
}
