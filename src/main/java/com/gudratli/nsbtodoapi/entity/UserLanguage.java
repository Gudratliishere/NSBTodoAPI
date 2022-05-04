package com.gudratli.nsbtodoapi.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "user_languages")
@Data
@NoArgsConstructor
public class UserLanguage
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @JoinColumn(name = "user_id", referencedColumnName = "user_id")
    @ManyToOne(cascade = CascadeType.MERGE)
    private User user;
    @JoinColumn(name = "language_id", referencedColumnName = "language_id")
    @ManyToOne(cascade = CascadeType.MERGE)
    private Language language;
}
