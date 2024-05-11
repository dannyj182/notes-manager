package com.dannyj182.notesmanager.controller;

import com.dannyj182.notesmanager.model.dto.NoteDTO;
import com.dannyj182.notesmanager.model.dto.ResponseDTO;
import com.dannyj182.notesmanager.model.entity.Note;
import com.dannyj182.notesmanager.service.INoteService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/notes")
@AllArgsConstructor
public class NoteController {

    private final INoteService service;

    @GetMapping("/")
    public ResponseEntity<Page<Note>> findNotesByUser(@RequestParam(defaultValue = "0") int page,
                                                      @RequestParam(defaultValue = "10") int elements,
                                                      @RequestParam(defaultValue = "modifiedDate") String[] sortBy,
                                                      @RequestParam(defaultValue = "DESC") String sortDirection){
        return new ResponseEntity<>(service.findNotesByUsername(page, elements, sortBy, sortDirection), HttpStatus.OK);
    }

    @PostMapping("/")
    public ResponseEntity<Object> saveNote(@RequestBody NoteDTO noteDTO){
        ResponseDTO res = service.saveNote(noteDTO);
        return new ResponseEntity<>(res.getBody(), res.getStatus());
    }

    @PutMapping("/{noteId}")
    public ResponseEntity<NoteDTO> editNote(@PathVariable long noteId, @RequestBody NoteDTO noteDTO){
        NoteDTO noteDTOEdited = service.editNote(noteId, noteDTO);
        if (noteDTOEdited != null) return new ResponseEntity<>(noteDTOEdited, HttpStatus.OK);
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("/{noteId}")
    public ResponseEntity<?> deleteById(@PathVariable long noteId){
        if (service.deleteById(noteId)) return new ResponseEntity<>(HttpStatus.OK);
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}
