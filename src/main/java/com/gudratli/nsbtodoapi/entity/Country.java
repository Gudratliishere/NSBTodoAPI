package com.gudratli.nsbtodoapi.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "countries")
@Data
@NoArgsConstructor
@RequiredArgsConstructor
public class Country
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "country_id")
    private Integer id;
    @Column(name = "name")
    @NonNull
    private String name;
    @JoinColumn(name = "region_id", referencedColumnName = "region_id")
    @ManyToOne(cascade = CascadeType.MERGE)
    @NonNull
    private Region region;
}
