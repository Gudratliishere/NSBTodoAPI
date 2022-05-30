package com.gudratli.nsbtodoapi.service.impl;

import com.gudratli.nsbtodoapi.entity.Process;
import com.gudratli.nsbtodoapi.exception.duplicate.DuplicateProcessException;
import com.gudratli.nsbtodoapi.repository.ProcessRepository;
import com.gudratli.nsbtodoapi.repository.TaskRepository;
import com.gudratli.nsbtodoapi.repository.UserRepository;
import com.gudratli.nsbtodoapi.service.inter.ProcessService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class ProcessServiceImpl implements ProcessService
{
    private final ProcessRepository processRepository;
    private final UserRepository userRepository;
    private final TaskRepository taskRepository;

    @Override
    public List<Process> getAll ()
    {
        return processRepository.findAll();
    }

    @Override
    public List<Process> getByUserId (Integer id)
    {
        return processRepository.findByUser(userRepository.findById(id).orElse(null));
    }

    @Override
    public List<Process> getByTaskId (Integer id)
    {
        return processRepository.findByTask(taskRepository.findById(id).orElse(null));
    }

    @Override
    public Process getById (Integer id)
    {
        return processRepository.findById(id).orElse(null);
    }

    @Override
    public Process add (Process process) throws DuplicateProcessException
    {
        checkForDuplicate(process);

        return processRepository.save(process);
    }

    @Override
    public Process update (Process process) throws DuplicateProcessException
    {
        checkForDuplicate(process);

        return processRepository.save(process);
    }

    @Override
    public void remove (Integer id)
    {
        processRepository.findById(id).ifPresent(processRepository::delete);
    }

    private void checkForDuplicate (Process process) throws DuplicateProcessException
    {
        Process test = processRepository.findByUserAndTask(process.getUser(), process.getTask());

        if (test != null && !test.getId().equals(process.getId()))
            throw new DuplicateProcessException();
    }
}
