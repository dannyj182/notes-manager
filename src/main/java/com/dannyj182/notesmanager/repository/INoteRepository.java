package com.dannyj182.notesmanager.repository;

import com.dannyj182.notesmanager.model.entity.Note;
import com.dannyj182.notesmanager.model.entity.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface INoteRepository extends JpaRepository<Note, Long> {
    List<Note> findAllByUser_Username(String username);
    List<Note> findAllByTagsContains(Tag tag);
}
