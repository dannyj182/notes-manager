package com.dannyj182.notesmanager.service;

import com.dannyj182.notesmanager.model.dto.NoteDTO;
import com.dannyj182.notesmanager.model.dto.ResponseDTO;

public interface INoteService {
    ResponseDTO saveNote(NoteDTO noteDTO);
    ResponseDTO findNotesByUser(Integer pageNumber, Integer pageSize, String[] sortBy, String sortDirection);
    boolean deleteById(long noteId);
    NoteDTO editNote(long noteId, NoteDTO noteDTO);
}
