package com.dannyj182.notesmanager.service;

import com.dannyj182.notesmanager.model.dto.NoteDTO;
import com.dannyj182.notesmanager.model.dto.ResponseDTO;

import java.util.List;

public interface INoteService {
    ResponseDTO findNotesByUser(Long noteId, Integer pageNumber, Integer pageSize, String[] sortBy, String sortDirection);

    ResponseDTO saveNotes(List<NoteDTO> noteDTOList);

    ResponseDTO editNotes(List<NoteDTO> noteDTOList);

    ResponseDTO deleteNotes(List<Long> noteIds);
}
