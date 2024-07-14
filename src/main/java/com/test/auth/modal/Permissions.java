package com.test.auth.modal;


import lombok.Data;

import jakarta.persistence.*;

@Data
@Table(name = "permissions")
@Entity
public class Permissions {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    @Id
    public Long id;

    @Column(name = "name", nullable = false)
    private String name;
}
