package com.dannyj182.notesmanager.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class User {

    @Id
    @Column(nullable = false, length = 20)
    private String userName;
    @Column(nullable = false, length = 200)
    private String password;
    private String name;
    private String lastName;
}
