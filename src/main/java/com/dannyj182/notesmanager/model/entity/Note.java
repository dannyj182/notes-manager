package com.dannyj182.notesmanager.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.List;

@Entity
@EntityListeners(AuditingEntityListener.class)
@Getter @Setter
@NoArgsConstructor
public class Note extends Auditable{

    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private Long noteId;

    private String title;
    private String description;

    @ManyToOne
    @JoinColumn(nullable = false, name = "username")
    @JsonIgnore
    private User user;

    @ManyToOne
    @JoinColumn(nullable = false, name = "status")
    private Status status;

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH})
    @Fetch(FetchMode.JOIN)
    @JoinTable(name = "note_tag", joinColumns = @JoinColumn(name = "note_id"), inverseJoinColumns = @JoinColumn(name = "tag_name"))
    private List<Tag> tags;
}
