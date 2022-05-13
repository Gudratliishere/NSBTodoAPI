package com.gudratli.nsbtodoapi.service.inter;

import com.gudratli.nsbtodoapi.entity.Conversation;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ConversationService
{
    List<Conversation> getAll ();

    List<Conversation> getByUserId (Integer id);

    List<Conversation> getByProcessId (Integer id);

    Conversation getById (Integer id);

    Conversation add (Conversation conversation);

    Conversation update (Conversation conversation);

    void remove (Integer id);
}
