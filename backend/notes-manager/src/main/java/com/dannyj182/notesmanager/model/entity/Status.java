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
public class Status {

    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private Long statusId;
    @Column(nullable = false, unique = true)
    private String status;
}
