package com.gudratli.nsbtodoapi.service.inter;

import com.gudratli.nsbtodoapi.entity.Process;
import org.springframework.stereotype.Service;

import java.util.List;

public interface ProcessService
{
    List<Process> getAll ();

    List<Process> getUserId (Integer id);

    List<Process> getTaskId (Integer id);

    Process getById (Integer id);

    Process add (Process process);

    Process update (Process process);

    void remove (Integer id);

}
