package com.dannyj182.notesmanager.service;

import com.dannyj182.notesmanager.model.dto.NoteDTO;
import com.dannyj182.notesmanager.model.entity.Note;
import org.springframework.data.domain.Page;

public interface INoteService {
    NoteDTO saveNote(NoteDTO noteDTO);
    Page<Note> findNotesByUsername(int page, int elements);
    boolean deleteById(long noteId);
    NoteDTO editNote(long noteId, NoteDTO noteDTO);
}
