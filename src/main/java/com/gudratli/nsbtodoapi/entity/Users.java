package com.gudratli.nsbtodoapi.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import javax.persistence.*;

@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
public class Users
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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
    @Column(name = "cv")
    @NonNull
    private String username;
    @Column(name = "password")
    @NonNull
    private String password;
//    @JoinColumn(name = "country_id", referencedColumnName = "country_id")
//    @ManyToOne(cascade = CascadeType.MERGE)
//    @NonNull
//    private Country country;
    @JoinColumn(name = "role_id", referencedColumnName = "role_id")
    @ManyToOne(cascade = CascadeType.MERGE)
    @NonNull
    private Roles role;
}
