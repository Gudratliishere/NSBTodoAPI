package com.gudratli.nsbtodoapi.entity;

import lombok.*;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "roles")
@Data
@NoArgsConstructor
public class Role
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "role_id")
    private Integer id;
    @Column(name = "name")
    @NonNull
    private String name;
    @NonNull
    @Column(name = "role_description")
    private String roleDescription;
}
