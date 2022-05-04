package com.gudratli.nsbtodoapi.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "languages")
@Data
@NoArgsConstructor
public class Languages
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name = "name")
    @NonNull
    private String name;
    @OneToMany(mappedBy = "language")
    private List<UserLanguages> userLanguages;
}
