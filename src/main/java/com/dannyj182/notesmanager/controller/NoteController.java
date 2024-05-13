package com.dannyj182.notesmanager.controller;

import com.dannyj182.notesmanager.model.dto.NoteDTO;
import com.dannyj182.notesmanager.model.dto.ResponseDTO;
import com.dannyj182.notesmanager.service.INoteService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/notes")
@AllArgsConstructor
public class NoteController {

    private final INoteService service;

    @GetMapping("/")
    public ResponseEntity<Object> findNotesByUser(@RequestParam(defaultValue = "0") Integer pageNumber,
                                                  @RequestParam(defaultValue = "10") Integer pageSize,
                                                  @RequestParam(defaultValue = "modifiedDate") String[] sortBy,
                                                  @RequestParam(defaultValue = "DESC") String sortDirection){
        ResponseDTO res = service.findNotesByUser(pageNumber, pageSize, sortBy, sortDirection);
        return new ResponseEntity<>(res.getBody(), res.getStatus());
    }

    @PostMapping("/")
    public ResponseEntity<Object> saveNote(@RequestBody NoteDTO noteDTO){
        ResponseDTO res = service.saveNote(noteDTO);
        return new ResponseEntity<>(res.getBody(), res.getStatus());
    }

    @PutMapping("/{noteId}")
    public ResponseEntity<Object> editNote(@PathVariable Long noteId, @RequestBody NoteDTO noteDTO){
        ResponseDTO res = service.editNote(noteId, noteDTO);
        return new ResponseEntity<>(res.getBody(), res.getStatus());
    }

    @DeleteMapping("/{noteId}")
    public ResponseEntity<Object> deleteNote(@PathVariable Long noteId){
        ResponseDTO res = service.deleteNote(noteId);
        return new ResponseEntity<>(res.getBody(), res.getStatus());
    }
}
