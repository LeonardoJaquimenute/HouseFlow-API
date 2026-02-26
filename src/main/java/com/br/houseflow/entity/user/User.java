package com.br.houseflow.entity.user;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "users")
public class User {

    @Id @GeneratedValue
    private Long id;

    @Email
    private String email;

    @Enumerated(EnumType.STRING)
    private Role role;

    private String name;
    private String password;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private User parent;

    @JsonIgnore
    @OneToMany(mappedBy = "parent")
    private List<User> children = new ArrayList<>();
}