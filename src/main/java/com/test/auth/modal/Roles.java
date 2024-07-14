package com.test.auth.modal;


import jakarta.persistence.*;
import lombok.Data;

@Data
@Table(name = "roles")
@Entity
public class Roles {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    @Id
    public Long id;

    @Column(name = "name", nullable = false)
    private String name;
}
