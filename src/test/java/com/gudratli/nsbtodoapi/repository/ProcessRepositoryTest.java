package com.gudratli.nsbtodoapi.repository;

import com.gudratli.nsbtodoapi.NsbTodoApiApplication;
import com.gudratli.nsbtodoapi.entity.Process;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.transaction.Transactional;
import java.util.List;

import static com.gudratli.nsbtodoapi.util.Entities.*;
import static org.assertj.core.util.DateUtil.parse;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

@ExtendWith(SpringExtension.class)
@Transactional
@SpringBootTest(classes = NsbTodoApiApplication.class)
class ProcessRepositoryTest
{
    @Autowired
    private ProcessRepository processRepository;

    @Test
    public void testFindById ()
    {
        Process expected = getProcess();
        Process actual = processRepository.findById(1).orElse(null);

        assertEquals(expected, actual);
    }

    @Test
    public void testFindByUser ()
    {
        List<Process> expected = getProcessList();
        List<Process> actual = processRepository.findByUser(getUser());

        assertEquals(expected, actual);
    }

    @Test
    public void testFindByTask ()
    {
        List<Process> expected = getProcessList();
        List<Process> actual = processRepository.findByTask(getTask());

        assertEquals(expected, actual);
    }

    @Test
    public void testFindAll ()
    {
        List<Process> expected = getProcessList();
        List<Process> actual = processRepository.findAll();

        assertEquals(expected, actual);
    }

    @Test
    public void testAddProcess ()
    {
        Process process = new Process(getUser(), getTask(), parse("2023-03-15 00:05:21"),
                parse("2022-05-12 12:08:23"), parse("2023-08-15 00:05:21"), getStatus());
        process = processRepository.save(process);

        Process expected = getProcess("3", process.getId());
        Process actual = processRepository.findById(process.getId()).orElse(null);

        assertEquals(expected, actual);
    }

    @Test
    public void testUpdateProcess ()
    {
        Process process = processRepository.findById(2).orElse(null);
        if (process != null)
        {
            process.setStart_date(parse("2024-03-15 00:05:21"));
            process.setEnd_date(parse("2024-08-15 00:05:21"));
            processRepository.save(process);
        }

        Process expected = getProcess("4", 2);
        Process actual = processRepository.findById(2).orElse(null);

        assertEquals(expected, actual);
    }

    @Test
    public void testDeleteProcess ()
    {
        processRepository.findById(2).ifPresent(process -> processRepository.delete(process));

        Process actual = processRepository.findById(2).orElse(null);

        assertNull(actual);
    }
}