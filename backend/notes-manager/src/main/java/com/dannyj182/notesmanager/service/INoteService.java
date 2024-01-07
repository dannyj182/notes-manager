package com.dannyj182.notesmanager.service;

import com.dannyj182.notesmanager.model.dto.NoteDTO;

import java.util.List;

public interface INoteService {
    NoteDTO saveNote(NoteDTO noteDTO);
    List<NoteDTO> findNotesByUserName();
    boolean deleteById(long noteId);
    NoteDTO editNote(long noteId, NoteDTO noteDTO);
}
