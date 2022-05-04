package com.gudratli.nsbtodoapi.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "user_languages")
@Data
@NoArgsConstructor
public class UserLanguages
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @JoinColumn(name = "user_id", referencedColumnName = "user_id")
    @ManyToOne(cascade = CascadeType.MERGE)
    private Users user;
    @JoinColumn(name = "language_id", referencedColumnName = "language_id")
    @ManyToOne(cascade = CascadeType.MERGE)
    private Languages language;
}
