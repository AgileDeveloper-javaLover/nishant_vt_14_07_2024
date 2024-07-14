package com.test.auth.modal;


import jakarta.persistence.*;
import lombok.Data;

@Data
@Table(name = "user_role")
@Entity
public class UserRole {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    @Id
    public Long id;

//    @Column(name = "role_id", nullable = false)
    @ManyToOne
    @JoinColumn(name="role_id", nullable=false)
    public Roles role;

//    @Column(name = "permission_id", nullable = false)
    @ManyToOne
    @JoinColumn(name="user_id", nullable=false)
    private Users user;
}
