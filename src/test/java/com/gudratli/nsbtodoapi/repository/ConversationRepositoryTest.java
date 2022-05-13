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
class ConversationRepositoryTest
{
    @Autowired
    private ConversationRepository conversationRepository;

    @Test
    public void testFindById ()
    {
        Conversation expected = getConversation();
        Conversation actual = conversationRepository.findById(1).orElse(null);

        assertEquals(expected, actual);
    }

    @Test
    public void testFindByProcess ()
    {
        List<Conversation> expected = getConversations();
        List<Conversation> actual = conversationRepository.findByProcess(getProcess());

        assertEquals(expected, actual);
    }

    @Test
    public void testFindByUser ()
    {
        List<Conversation> expected = getConversations();
        List<Conversation> actual = conversationRepository.findByUser(getUser());

        assertEquals(expected, actual);
    }

    @Test
    public void testFindAll ()
    {
        List<Conversation> expected = getConversations();
        List<Conversation> actual = conversationRepository.findAll();

        assertEquals(expected, actual);
    }

    @Test
    public void testAddConversation ()
    {
        Conversation conversation = new Conversation(getUser(), getProcess(), "Fine", parse("2022-05-13 22:37:05"));
        conversation = conversationRepository.save(conversation);

        Conversation expected = getConversation("Fine", conversation.getId());
        Conversation actual = conversationRepository.findById(conversation.getId()).orElse(null);

        assertEquals(expected, actual);
    }

    @Test
    public void testUpdateConversation ()
    {
        Conversation conversation = conversationRepository.findById(2).orElse(null);
        if (conversation != null)
        {
            conversation.setMessage("HowUpdated");
            conversationRepository.save(conversation);
        }

        Conversation expected = getConversation("HowUpdated", 2);
        Conversation actual = conversationRepository.findById(2).orElse(null);

        assertEquals(expected, actual);
    }

    @Test
    public void testDeleteConversation ()
    {
        conversationRepository.findById(2).ifPresent(conversation -> conversationRepository.delete(conversation));

        Conversation actual = conversationRepository.findById(2).orElse(null);

        assertNull(actual);
    }

    private Conversation getConversation ()
    {
        Conversation conversation = new Conversation(getUser(), getProcess(), "Hello", parse("2022-05-13 22:37:05"));
        conversation.setId(1);
        return conversation;
    }

    private Conversation getConversation (String message, int id)
    {
        Conversation conversation = new Conversation(getUser(), getProcess(), message, parse("2022-05-13 22:37:05"));
        conversation.setId(id);
        return conversation;
    }

    private Process getProcess ()
    {
        Process process = new Process(getUser(), getTask(), parse("2022-03-15 00:05:21"),
                parse("2022-05-12 12:08:23"), parse("2022-08-15 00:05:21"), getStatus());
        process.setId(1);
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

    private List<Conversation> getConversations ()
    {
        return Arrays.asList(getConversation(), getConversation("How", 2));
    }
}