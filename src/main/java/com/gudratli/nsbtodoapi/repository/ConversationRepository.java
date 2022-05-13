package com.gudratli.nsbtodoapi.repository;

import com.gudratli.nsbtodoapi.entity.Conversation;
import com.gudratli.nsbtodoapi.entity.Process;
import com.gudratli.nsbtodoapi.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ConversationRepository extends JpaRepository<Conversation, Integer>
{
    List<Conversation> findByProcess (Process process);

    List<Conversation> findByUser (User user);
}