package com.gudratli.nsbtodoapi.repository;

import com.gudratli.nsbtodoapi.NsbTodoApiApplication;
import com.gudratli.nsbtodoapi.entity.Task;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.transaction.Transactional;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@Transactional
@SpringBootTest(classes = NsbTodoApiApplication.class)
class TaskRepositoryTest
{
    @Autowired
    private TaskRepository taskRepository;

    @Test
    public void testFindById ()
    {
        Task expected = getTask();
        Task actual = taskRepository.findById(1).orElse(null);

        assertEquals(expected, actual);
    }

    @Test
    public void testFindByNameContaining ()
    {
        List<Task> expected = getTasks();
        List<Task> actual = taskRepository.findByNameContaining("e");

        assertEquals(expected, actual);
    }

    @Test
    public void testFindAll ()
    {
        List<Task> expected = getTasks();
        List<Task> actual = taskRepository.findAll();

        assertEquals(expected, actual);
    }

    @Test
    public void testAddTask ()
    {
        Task task = new Task("Make clean", "You will rename project", "doc", "result");
        task = taskRepository.save(task);

        Task expected = getTask("Make clean", 3);
        Task actual = taskRepository.findById(task.getId()).orElse(null);

        assertEquals(expected, actual);
    }

    @Test
    public void testUpdateTask ()
    {
        Task task = taskRepository.findById(2).orElse(null);
        if (task != null)
        {
            task.setName("Delete equals Updated");
            taskRepository.save(task);
        }

        Task expected = getTask("Delete equals Updated", 2);
        Task actual = taskRepository.findById(2).orElse(null);

        assertEquals(expected, actual);
    }

    @Test
    public void testDeleteTask ()
    {
        taskRepository.findById(2).ifPresent(task -> taskRepository.delete(task));

        Task actual = taskRepository.findById(2).orElse(null);

        assertNull(actual);
    }

    private Task getTask ()
    {
        Task task = new Task("Rename project", "You will rename project", "doc", "result");
        task.setId(1);
        return task;
    }

    private Task getTask (String name, int id)
    {
        Task task = new Task(name, "You will rename project", "doc", "result");
        task.setId(id);
        return task;
    }

    private List<Task> getTasks ()
    {
        return Arrays.asList(getTask(), getTask("Delete equals", 2));
    }
}