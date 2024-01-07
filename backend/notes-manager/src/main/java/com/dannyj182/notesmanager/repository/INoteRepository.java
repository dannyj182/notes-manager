package com.dannyj182.notesmanager.repository;

import com.dannyj182.notesmanager.model.entity.Note;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface INoteRepository extends CrudRepository<Note, Long> {
    List<Note> findAllByUser_UserName(String userName);
}
