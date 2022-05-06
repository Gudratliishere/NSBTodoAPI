package com.gudratli.nsbtodoapi.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "user_technologies")
@Data
@NoArgsConstructor
public class UserTechnology
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_technology_id")
    private Integer id;
    @JoinColumn(name = "user_id", referencedColumnName = "user_id")
    @ManyToOne(cascade = CascadeType.MERGE)
    @NonNull
    private User user;
    @JoinColumn(name = "technology_id", referencedColumnName = "technology_id")
    @ManyToOne(cascade = CascadeType.MERGE)
    @NonNull
    private Technology technology;
    @Column(name = "level")
    @NonNull
    private Integer level;
}
