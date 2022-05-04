package com.gudratli.nsbtodoapi.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "process")
@Data
@NoArgsConstructor
public class Process
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @JoinColumn(name = "user_id", referencedColumnName = "user_id")
    @ManyToOne(cascade = CascadeType.MERGE)
    @NonNull
    private User user;
    @JoinColumn(name = "task_id", referencedColumnName = "task_id")
    @ManyToOne(cascade = CascadeType.MERGE)
    @NonNull
    private Task task;
    @Column(name = "start_date")
    @NonNull
    private Date start_date;
    @Column(name = "deadline")
    @NonNull
    private Date deadline;
    @Column(name = "end_date")
    @NonNull
    private Date end_date;
    @JoinColumn(name = "status_id", referencedColumnName = "status_id")
    @ManyToOne(cascade = CascadeType.MERGE)
    @NonNull
    private Status status;
    @OneToMany(mappedBy = "process")
    private List<Conversation> conversations;
}
