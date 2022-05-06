package com.gudratli.nsbtodoapi.entity;

import lombok.*;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "regions")
@Data
@NoArgsConstructor
@RequiredArgsConstructor
public class Region
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "region_id")
    private Integer id;
    @Column(name = "name")
    @NonNull
    private String name;
}
