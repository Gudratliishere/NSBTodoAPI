package com.gudratli.nsbtodoapi.repository;

import com.gudratli.nsbtodoapi.entity.Conversation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ConversationRepository extends JpaRepository<Conversation, Integer>
{
}