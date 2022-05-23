package com.gudratli.nsbtodoapi.repository;

import com.gudratli.nsbtodoapi.NsbTodoApiApplication;
import com.gudratli.nsbtodoapi.entity.Status;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.transaction.Transactional;
import java.util.List;

import static com.gudratli.nsbtodoapi.util.Entities.getStatus;
import static com.gudratli.nsbtodoapi.util.Entities.getStatusList;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

@ExtendWith(SpringExtension.class)
@Transactional
@SpringBootTest(classes = NsbTodoApiApplication.class)
class StatusRepositoryTest
{
    @Autowired
    private StatusRepository statusRepository;

    @Test
    public void testFindById ()
    {
        Status expected = getStatus();
        Status actual = statusRepository.findById(1).orElse(null);

        assertEquals(expected, actual);
    }

    @Test
    public void testFindAll ()
    {
        List<Status> expected = getStatusList();
        List<Status> actual = statusRepository.findAll();

        assertEquals(expected, actual);
    }

    @Test
    public void testAddRole ()
    {
        Status status = new Status("Late");
        status = statusRepository.save(status);

        Status actual = statusRepository.findById(status.getId()).orElse(null);
        Status expected = getStatus("Late", 3);

        assertEquals(expected, actual);
    }

    @Test
    public void testUpdateRole ()
    {
        Status status = statusRepository.findById(2).orElse(null);
        if (status != null)
        {
            status.setName("IncompleteUpdated");
            statusRepository.save(status);
        }

        Status actual = statusRepository.findById(2).orElse(null);
        Status expected = getStatus("IncompleteUpdated", 2);

        assertEquals(expected, actual);
    }

    @Test
    public void testDeleteRole ()
    {
        statusRepository.findById(2).ifPresent(status -> statusRepository.delete(status));

        Status actual = statusRepository.findById(2).orElse(null);

        assertNull(actual);
    }
}