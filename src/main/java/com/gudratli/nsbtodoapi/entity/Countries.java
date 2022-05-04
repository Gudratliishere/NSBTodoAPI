package com.gudratli.nsbtodoapi.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "countries")
@Data
@NoArgsConstructor
public class Countries
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name = "name")
    @NonNull
    private String name;
    @JoinColumn(name = "region_id", referencedColumnName = "region_id")
    @ManyToOne(cascade = CascadeType.MERGE)
    @NonNull
    private Regions region;
    @OneToMany(mappedBy = "country")
    private List<Users> users;
}
