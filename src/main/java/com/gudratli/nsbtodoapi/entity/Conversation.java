package com.gudratli.nsbtodoapi.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "conversations")
@Data
@NoArgsConstructor
@RequiredArgsConstructor
public class Conversation
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "conversation_id")
    private Integer id;
    @JoinColumn(name = "user_id", referencedColumnName = "user_id")
    @ManyToOne(cascade = CascadeType.MERGE)
    @NonNull
    private User user;
    @JoinColumn(name = "process_id", referencedColumnName = "process_id")
    @ManyToOne(cascade = CascadeType.MERGE)
    @NonNull
    private Process process;
    @Column(name = "message")
    @NonNull
    private String message;
    @Column(name = "send_date")
    @NonNull
    private Date sendDate;
}
