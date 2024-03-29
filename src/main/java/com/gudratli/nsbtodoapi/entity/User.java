package com.gudratli.nsbtodoapi.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
@RequiredArgsConstructor
public class User
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Integer id;
    @Column(name = "name")
    @NonNull
    private String name;
    @Column(name = "surname")
    @NonNull
    private String surname;
    @Column(name = "phone")
    @NonNull
    private String phone;
    @Column(name = "email")
    @NonNull
    private String email;
    @Column(name = "github")
    @NonNull
    private String github;
    @Column(name = "address")
    @NonNull
    private String address;
    @Column(name = "username")
    @NonNull
    private String username;
    @Column(name = "password")
    @NonNull
    private String password;
    @Column(name = "status")
    private Boolean status;
    @Column(name = "banned")
    private Boolean banned;
    @JoinColumn(name = "cv", referencedColumnName = "id")
    @OneToOne
    @NonNull
    private File cv;
    @JoinColumn(name = "country_id", referencedColumnName = "country_id")
    @ManyToOne(cascade = CascadeType.MERGE)
    @NonNull
    private Country country;
    @JoinColumn(name = "role_id", referencedColumnName = "role_id")
    @ManyToOne(cascade = CascadeType.MERGE)
    @NonNull
    private Role role;
}

