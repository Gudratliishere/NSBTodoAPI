package com.gudratli.nsbtodoapi.repository;

import com.gudratli.nsbtodoapi.NsbTodoApiApplication;
import com.gudratli.nsbtodoapi.entity.Process;
import com.gudratli.nsbtodoapi.entity.*;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.transaction.Transactional;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

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
        List<Process> expected = getProcesses();
        List<Process> actual = processRepository.findByUser(getUser());

        assertEquals(expected, actual);
    }

    @Test
    public void testFindByTask ()
    {
        List<Process> expected = getProcesses();
        List<Process> actual = processRepository.findByTask(getTask());

        assertEquals(expected, actual);
    }

    @Test
    public void testFindAll ()
    {
        List<Process> expected = getProcesses();
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

    private Process getProcess ()
    {
        Process process = new Process(getUser(), getTask(), parse("2022-03-15 00:05:21"),
                parse("2022-05-12 12:08:23"), parse("2022-08-15 00:05:21"), getStatus());
        process.setId(1);
        return process;
    }

    private Process getProcess (String year, int id)
    {
        Process process = new Process(getUser(), getTask(), parse("202" + year + "-03-15 00:05:21"),
                parse("2022-05-12 12:08:23"), parse("202" + year + "-08-15 00:05:21"), getStatus());
        process.setId(id);
        return process;
    }

    private User getUser ()
    {
        User user = new User("Dunay", "Gudratli", "0556105884", "dunay@gmail", "git",
                "masazir", "cv", "dunay", "123", getCountry(), getRole());
        user.setId(6);
        user.setStatus(true);
        user.setBanned(false);
        return user;
    }

    private Role getRole ()
    {
        Role role = new Role("USER", "User");
        role.setId(1);
        return role;
    }

    private Country getCountry ()
    {
        Country country = new Country("UK", getRegion());
        country.setId(1);
        return country;
    }

    private Region getRegion ()
    {
        Region region = new Region("Asia");
        region.setId(1);
        return region;
    }

    private Task getTask ()
    {
        Task task = new Task("Rename project", "You will rename project", "doc", "result");
        task.setId(1);
        return task;
    }

    private Status getStatus ()
    {
        Status status = new Status("Finished");
        status.setId(1);
        return status;
    }

    @SneakyThrows
    private Date parse (String date)
    {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return formatter.parse(date);
    }

    private List<Process> getProcesses ()
    {
        return Arrays.asList(getProcess(), getProcess("1", 2));
    }
}