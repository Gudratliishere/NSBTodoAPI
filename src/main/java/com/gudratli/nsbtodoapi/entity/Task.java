package com.gudratli.nsbtodoapi.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "tasks")
@Data
@NoArgsConstructor
public class Task
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name = "name")
    @NonNull
    private String name;
    @Column(name = "description")
    @NonNull
    private String description;
    @Column(name = "documentation")
    @NonNull
    private String documentation;
    @Column(name = "result")
    @NonNull
    private String result;
    @OneToMany(mappedBy = "task")
    private List<Process> processes;
}
