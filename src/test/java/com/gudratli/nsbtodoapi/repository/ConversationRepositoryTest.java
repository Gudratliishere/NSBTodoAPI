package com.gudratli.nsbtodoapi.repository;

import com.gudratli.nsbtodoapi.NsbTodoApiApplication;
import com.gudratli.nsbtodoapi.entity.Conversation;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.transaction.Transactional;
import java.util.List;

import static com.gudratli.nsbtodoapi.util.Entities.*;
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
        List<Conversation> expected = getConversationList();
        List<Conversation> actual = conversationRepository.findByProcess(getProcess());

        assertEquals(expected, actual);
    }

    @Test
    public void testFindByUser ()
    {
        List<Conversation> expected = getConversationList();
        List<Conversation> actual = conversationRepository.findByUser(getUser());

        assertEquals(expected, actual);
    }

    @Test
    public void testFindAll ()
    {
        List<Conversation> expected = getConversationList();
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
}