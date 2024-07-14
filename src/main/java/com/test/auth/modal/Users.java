package com.test.auth.modal;


import jakarta.persistence.*;
import lombok.Data;

@Data
@Table(name = "users")
@Entity
public class Users {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    @Id
    public Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "email", nullable = false,unique = true)
    private String email;

    @Column(name = "password", nullable = false)
    private String password;
}
