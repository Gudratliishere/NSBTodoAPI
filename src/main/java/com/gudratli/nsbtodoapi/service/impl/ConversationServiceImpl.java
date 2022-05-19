package com.gudratli.nsbtodoapi.service.impl;

import com.gudratli.nsbtodoapi.entity.Conversation;
import com.gudratli.nsbtodoapi.repository.ConversationRepository;
import com.gudratli.nsbtodoapi.repository.ProcessRepository;
import com.gudratli.nsbtodoapi.repository.UserRepository;
import com.gudratli.nsbtodoapi.service.inter.ConversationService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ConversationServiceImpl implements ConversationService
{
    private final ConversationRepository conversationRepository;
    private final UserRepository userRepository;
    private final ProcessRepository processRepository;

    public ConversationServiceImpl (
            ConversationRepository conversationRepository,
            UserRepository userRepository, ProcessRepository processRepository)
    {
        this.conversationRepository = conversationRepository;
        this.userRepository = userRepository;
        this.processRepository = processRepository;
    }

    @Override
    public List<Conversation> getAll ()
    {
        return conversationRepository.findAll();
    }

    @Override
    public List<Conversation> getByUserId (Integer id)
    {
        return conversationRepository.findByUser(userRepository.findById(id).orElse(null));
    }

    @Override
    public List<Conversation> getByProcessId (Integer id)
    {
        return conversationRepository.findByProcess(processRepository.findById(id).orElse(null));
    }

    @Override
    public List<Conversation> getByUserIdAndProcessId (Integer userId, Integer processId)
    {
        return conversationRepository.findByUserAndProcess(userRepository.findById(userId).orElse(null),
                processRepository.findById(processId).orElse(null));
    }

    @Override
    public Conversation getById (Integer id)
    {
        return conversationRepository.findById(id).orElse(null);
    }

    @Override
    public Conversation add (Conversation conversation)
    {
        return conversationRepository.save(conversation);
    }

    @Override
    public Conversation update (Conversation conversation)
    {
        return conversationRepository.save(conversation);
    }

    @Override
    public void remove (Integer id)
    {
        conversationRepository.findById(id).ifPresent(conversationRepository::delete);
    }
}
