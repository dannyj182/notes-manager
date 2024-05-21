package com.dannyj182.notesmanager.controller;

import com.dannyj182.notesmanager.model.dto.NoteDTO;
import com.dannyj182.notesmanager.model.dto.ResponseDTO;
import com.dannyj182.notesmanager.service.INoteService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/notes")
@AllArgsConstructor
public class NoteController {

    private final INoteService service;

    @GetMapping(value = {"/", "/{noteId}"})
    public ResponseEntity<Object> findNotesByUser(@PathVariable(required = false) Long noteId,
                                                  @RequestParam(defaultValue = "0") Integer pageNumber,
                                                  @RequestParam(defaultValue = "10") Integer pageSize,
                                                  @RequestParam(defaultValue = "modifiedDate") String[] sortBy,
                                                  @RequestParam(defaultValue = "DESC") String sortDirection) {
        ResponseDTO res = service.findNotesByUser(noteId, pageNumber, pageSize, sortBy, sortDirection);
        return new ResponseEntity<>(res.getBody(), res.getStatus());
    }

    @PostMapping("/")
    public ResponseEntity<Object> saveNotes(@RequestBody List<NoteDTO> noteDTOList) {
        ResponseDTO res = service.saveNotes(noteDTOList);
        return new ResponseEntity<>(res.getBody(), res.getStatus());
    }

    @PutMapping("/{noteId}")
    public ResponseEntity<Object> editNote(@PathVariable Long noteId, @RequestBody NoteDTO noteDTO) {
        ResponseDTO res = service.editNote(noteId, noteDTO);
        return new ResponseEntity<>(res.getBody(), res.getStatus());
    }

    @DeleteMapping("/")
    public ResponseEntity<Object> deleteNotes(@RequestBody List<Long> noteIds) {
        ResponseDTO res = service.deleteNotes(noteIds);
        return new ResponseEntity<>(res.getBody(), res.getStatus());
    }
}
