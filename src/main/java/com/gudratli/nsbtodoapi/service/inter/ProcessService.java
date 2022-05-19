package com.gudratli.nsbtodoapi.service.inter;

import com.gudratli.nsbtodoapi.entity.Process;
import com.gudratli.nsbtodoapi.exception.duplicate.DuplicateProcessException;
import org.springframework.stereotype.Service;

import java.util.List;

public interface ProcessService
{
    List<Process> getAll ();

    List<Process> getByUserId (Integer id);

    List<Process> getByTaskId (Integer id);

    Process getById (Integer id);

    Process add (Process process) throws DuplicateProcessException;

    Process update (Process process) throws DuplicateProcessException;

    void remove (Integer id);

}
