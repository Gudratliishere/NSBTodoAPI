package com.gudratli.nsbtodoapi.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "regions")
@Data
@NoArgsConstructor
public class Regions
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name = "name")
    @NonNull
    private String name;
    @OneToMany(mappedBy = "region")
    private List<Regions> regions;
}
