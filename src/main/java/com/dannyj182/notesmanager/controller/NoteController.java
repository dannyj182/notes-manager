package com.dannyj182.notesmanager.controller;

import com.dannyj182.notesmanager.model.dto.NoteDTO;
import com.dannyj182.notesmanager.service.INoteService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/notes")
@AllArgsConstructor
public class NoteController {

    private final INoteService service;

    @PostMapping("/")
    public ResponseEntity<NoteDTO> saveNote(@RequestBody NoteDTO noteDTO){
        NoteDTO noteDTOSaved = service.saveNote(noteDTO);
        if (noteDTOSaved != null) return new ResponseEntity<>(noteDTOSaved, HttpStatus.CREATED);
        return new ResponseEntity<>(HttpStatus.CONFLICT);
    }

    @GetMapping("/")
    public ResponseEntity<List<NoteDTO>> findNotesByUser(){
        return new ResponseEntity<>(service.findNotesByUsername(), HttpStatus.OK);
    }

    @DeleteMapping("/{noteId}")
    public ResponseEntity<?> deleteById(@PathVariable long noteId){
        if (service.deleteById(noteId)) return new ResponseEntity<>(HttpStatus.OK);
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PutMapping("/{noteId}")
    public ResponseEntity<NoteDTO> editNote(@PathVariable long noteId, @RequestBody NoteDTO noteDTO){
        NoteDTO noteDTOEdited = service.editNote(noteId, noteDTO);
        if (noteDTOEdited != null) return new ResponseEntity<>(noteDTOEdited, HttpStatus.OK);
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}
