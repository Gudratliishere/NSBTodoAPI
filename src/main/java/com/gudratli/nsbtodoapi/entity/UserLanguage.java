package com.gudratli.nsbtodoapi.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "user_languages")
@Data
@NoArgsConstructor
@RequiredArgsConstructor
public class UserLanguage
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_language_id")
    private Integer id;
    @JoinColumn(name = "user_id", referencedColumnName = "user_id")
    @ManyToOne(cascade = CascadeType.MERGE)
    @NonNull
    private User user;
    @JoinColumn(name = "language_id", referencedColumnName = "language_id")
    @ManyToOne(cascade = CascadeType.MERGE)
    @NonNull
    private Language language;
    @Column(name = "level")
    @NonNull
    private Integer level;
}
