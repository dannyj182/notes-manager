package com.dannyj182.notesmanager.service;

import com.dannyj182.notesmanager.model.dto.NoteDTO;
import com.dannyj182.notesmanager.model.dto.ResponseDTO;

public interface INoteService {
    ResponseDTO findNotesByUser(Integer pageNumber, Integer pageSize, String[] sortBy, String sortDirection);
    ResponseDTO saveNote(NoteDTO noteDTO);
    ResponseDTO editNote(Long noteId, NoteDTO noteDTO);
    ResponseDTO deleteNote(Long noteId);
}
