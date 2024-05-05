package com.dannyj182.notesmanager.model.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter @Setter
@NoArgsConstructor
public class Tag {

    @Id
    @Column(length = 50)
    private String name;

    @ManyToOne
    @JoinColumn(nullable = false, name = "username")
    private User user;
}
