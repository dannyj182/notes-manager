package com.dannyj182.notesmanager.controller;

import com.dannyj182.notesmanager.model.dto.TagDTO;
import com.dannyj182.notesmanager.model.entity.Tag;
import com.dannyj182.notesmanager.service.ITagService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/tags")
@AllArgsConstructor
public class TagController {

    private final ITagService service;

    @GetMapping("/")
    public ResponseEntity<Page<Tag>> findTagByUsername(@RequestParam(defaultValue = "0") int page,
                                                       @RequestParam(defaultValue = "5") int elements,
                                                       @RequestParam(defaultValue = "name") String[] sortBy,
                                                       @RequestParam(defaultValue = "ASC") String sortDirection){
        return new ResponseEntity<>(service.findTagByUsername(page, elements, sortBy, sortDirection), HttpStatus.OK);
    }

    @PostMapping("/")
    public ResponseEntity<TagDTO> saveTag(@RequestBody TagDTO tagDTO){
        TagDTO tagDTOSaved = service.saveTag(tagDTO);
        if (tagDTOSaved != null) return new ResponseEntity<>(tagDTOSaved, HttpStatus.CREATED);
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @DeleteMapping("/{tagId}")
    public ResponseEntity<?> deleteById(@PathVariable String tagId){
        if (service.deleteById(tagId)) return new ResponseEntity<>(HttpStatus.OK);
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}
