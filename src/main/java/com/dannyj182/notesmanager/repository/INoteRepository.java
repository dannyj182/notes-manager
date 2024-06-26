package com.dannyj182.notesmanager.repository;

import com.dannyj182.notesmanager.model.entity.Note;
import com.dannyj182.notesmanager.model.entity.Tag;
import com.dannyj182.notesmanager.model.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface INoteRepository extends JpaRepository<Note, Long> {
    Page<Note> findAllByUser(User user, Pageable pageable);

    List<Note> findAllByTagsIn(List<Tag> tags);

    Optional<Note> findByNoteIdAndUser(Long noteId, User user);

    List<Note> findAllByNoteIdInAndUser(List<Long> noteIds, User user);
}
