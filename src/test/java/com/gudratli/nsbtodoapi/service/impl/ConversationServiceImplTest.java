package com.gudratli.nsbtodoapi.service.impl;

import com.gudratli.nsbtodoapi.entity.Conversation;
import com.gudratli.nsbtodoapi.entity.Process;
import com.gudratli.nsbtodoapi.entity.User;
import com.gudratli.nsbtodoapi.repository.ConversationRepository;
import com.gudratli.nsbtodoapi.repository.ProcessRepository;
import com.gudratli.nsbtodoapi.repository.UserRepository;
import com.gudratli.nsbtodoapi.service.inter.ConversationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static com.gudratli.nsbtodoapi.util.Entities.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.*;

class ConversationServiceImplTest
{
    private ConversationRepository conversationRepository;
    private UserRepository userRepository;
    private ProcessRepository processRepository;

    private ConversationService conversationService;

    @BeforeEach
    public void setUp ()
    {
        conversationRepository = mock(ConversationRepository.class);
        userRepository = mock(UserRepository.class);
        processRepository = mock(ProcessRepository.class);

        conversationService = new ConversationServiceImpl(conversationRepository, userRepository, processRepository);
    }

    @Test
    public void testGetAll ()
    {
        List<Conversation> expected = getConversationList();
        when(conversationRepository.findAll()).thenReturn(expected);

        List<Conversation> actual = conversationService.getAll();

        assertEquals(expected, actual);
        verify(conversationRepository).findAll();
    }

    @Test
    public void testGetByUserId ()
    {
        List<Conversation> expected = getConversationList();
        User user = getUser();
        when(conversationRepository.findByUser(user)).thenReturn(expected);
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));

        List<Conversation> actual = conversationService.getByUserId(user.getId());

        assertEquals(expected, actual);
        verify(conversationRepository).findByUser(user);
        verify(userRepository).findById(user.getId());
    }

    @Test
    public void testGetByProcessId ()
    {
        List<Conversation> expected = getConversationList();
        Process process = getProcess();
        when(conversationRepository.findByProcess(process)).thenReturn(expected);
        when(processRepository.findById(process.getId())).thenReturn(Optional.of(process));

        List<Conversation> actual = conversationService.getByProcessId(process.getId());

        assertEquals(expected, actual);
        verify(conversationRepository).findByProcess(process);
        verify(processRepository).findById(process.getId());
    }

    @Test
    public void testGetByUserIdAndProcessId ()
    {
        List<Conversation> expected = getConversationList();
        Process process = getProcess();
        User user = getUser();
        when(conversationRepository.findByUserAndProcess(user, process)).thenReturn(expected);
        when(processRepository.findById(process.getId())).thenReturn(Optional.of(process));
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));

        List<Conversation> actual = conversationService.getByUserIdAndProcessId(user.getId(), process.getId());

        assertEquals(expected, actual);
        verify(conversationRepository).findByUserAndProcess(user, process);
        verify(processRepository).findById(process.getId());
        verify(userRepository).findById(user.getId());
    }

    @Test
    public void testGetById ()
    {
        Conversation expected = getConversation();
        when(conversationRepository.findById(expected.getId())).thenReturn(Optional.of(expected));

        Conversation actual = conversationService.getById(expected.getId());

        assertEquals(expected, actual);
        verify(conversationRepository).findById(expected.getId());
    }

    @Test
    public void testAdd ()
    {
        Conversation conversation = new Conversation(getUser(), getProcess(), "Hello", parse("2022-05-13 22:37:05"));
        Conversation expected = getConversation(conversation.getMessage(), 5);
        when(conversationRepository.save(conversation)).thenReturn(expected);

        Conversation actual = conversationService.add(conversation);

        assertEquals(expected, actual);
        verify(conversationRepository).save(conversation);
    }

    @Test
    public void testUpdate ()
    {
        Conversation conversation = getConversation("hello", 4);
        Conversation expected = getConversation(conversation.getMessage(), conversation.getId());
        when(conversationRepository.save(conversation)).thenReturn(expected);

        Conversation actual = conversationService.update(conversation);

        assertEquals(expected, actual);
        verify(conversationRepository).save(conversation);
    }

    @Test
    public void testRemove ()
    {
        Conversation conversation = getConversation();
        when(conversationRepository.findById(conversation.getId())).thenReturn(Optional.of(conversation));

        conversationService.remove(conversation.getId());

        when(conversationRepository.findById(conversation.getId())).thenReturn(Optional.empty());

        Conversation actual = conversationService.getById(conversation.getId());

        assertNull(actual);
    }
}