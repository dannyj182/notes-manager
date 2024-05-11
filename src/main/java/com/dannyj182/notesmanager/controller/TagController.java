package com.dannyj182.notesmanager.controller;

import com.dannyj182.notesmanager.model.dto.ResponseDTO;
import com.dannyj182.notesmanager.model.dto.TagDTO;
import com.dannyj182.notesmanager.service.ITagService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/tags")
@AllArgsConstructor
public class TagController {

    private final ITagService service;

    @GetMapping("/")
    public ResponseEntity<Object> findTagsByUsername(@RequestParam(defaultValue = "0") int pageNumber,
                                                     @RequestParam(defaultValue = "5") int pageSize,
                                                     @RequestParam(defaultValue = "name") String[] sortBy,
                                                     @RequestParam(defaultValue = "ASC") String sortDirection){
        ResponseDTO res = service.findTagsByUsername(pageNumber, pageSize, sortBy, sortDirection);
        return new ResponseEntity<>(res.getBody(), res.getStatus());
    }

    @PostMapping("/")
    public ResponseEntity<TagDTO> saveTag(@RequestBody TagDTO tagDTO){
        TagDTO tagDTOSaved = service.saveTag(tagDTO);
        if (tagDTOSaved != null) return new ResponseEntity<>(tagDTOSaved, HttpStatus.CREATED);
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @PutMapping("/{tagId}")
    public ResponseEntity<Object> editTag(@PathVariable Long tagId, @RequestBody TagDTO tagDTO){
        ResponseDTO res = service.editTag(tagId, tagDTO);
        return new ResponseEntity<>(res.getBody(), res.getStatus());

    }

    @DeleteMapping("/{tagId}")
    public ResponseEntity<?> deleteById(@PathVariable Long tagId){
        if (service.deleteById(tagId)) return new ResponseEntity<>(HttpStatus.OK);
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}
