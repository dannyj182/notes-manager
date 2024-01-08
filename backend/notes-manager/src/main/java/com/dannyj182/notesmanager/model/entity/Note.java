package com.dannyj182.notesmanager.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Note {

    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private Long noteId;
    private String title;
    private String description;
    @ManyToOne
    @JoinColumn(name = "user_name")
    private User user;
    @ManyToOne
    @JoinColumn(name = "status_id")
    private Status status;
    @Column(nullable = false, columnDefinition = "DATETIME")
    private LocalDateTime modificationDate;
    @ManyToMany
    private List<Tag> tags;
}
