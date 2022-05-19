package com.gudratli.nsbtodoapi.service.inter;

import com.gudratli.nsbtodoapi.entity.Conversation;

import java.util.List;

public interface ConversationService
{
    List<Conversation> getAll ();

    List<Conversation> getByUserId (Integer id);

    List<Conversation> getByProcessId (Integer id);

    List<Conversation> getByUserIdAndProcessId (Integer userId, Integer processId);

    Conversation getById (Integer id);

    Conversation add (Conversation conversation);

    Conversation update (Conversation conversation);

    void remove (Integer id);
}
