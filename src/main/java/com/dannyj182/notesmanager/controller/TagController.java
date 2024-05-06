package com.dannyj182.notesmanager.controller;

import com.dannyj182.notesmanager.model.dto.TagDTO;
import com.dannyj182.notesmanager.service.ITagService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tags")
@AllArgsConstructor
public class TagController {

    private final ITagService service;

    @PostMapping("/")
    public ResponseEntity<TagDTO> saveTag(@RequestBody TagDTO tagDTO){
        TagDTO tagDTOSaved = service.saveTag(tagDTO);
        if (tagDTOSaved != null) return new ResponseEntity<>(tagDTOSaved, HttpStatus.OK);
        return new ResponseEntity<>(HttpStatus.CONFLICT);
    }

    @GetMapping("/")
    public ResponseEntity<List<TagDTO>> findTagByUsername(){
        return new ResponseEntity<>(service.findTagByUsername(), HttpStatus.OK);
    }

    @DeleteMapping("/{tagId}")
    public ResponseEntity<?> deleteById(@PathVariable String tagId){
        return service.deleteById(tagId);
    }
}
