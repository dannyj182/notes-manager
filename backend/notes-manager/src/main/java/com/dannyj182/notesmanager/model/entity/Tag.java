package com.dannyj182.notesmanager.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Tag {

    @Id
    @Column(nullable = false, length = 50)
    private String name;
    @ManyToMany (mappedBy = "tags")
    private List<Note> notes;
}
