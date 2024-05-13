package com.dannyj182.notesmanager.controller;

import com.dannyj182.notesmanager.model.dto.ResponseDTO;
import com.dannyj182.notesmanager.model.dto.TagDTO;
import com.dannyj182.notesmanager.service.ITagService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/tags")
@AllArgsConstructor
public class TagController {

    private final ITagService service;

    @GetMapping("/")
    public ResponseEntity<Object> findTagsByUser(@RequestParam(defaultValue = "0") Integer pageNumber,
                                                 @RequestParam(defaultValue = "5") Integer pageSize,
                                                 @RequestParam(defaultValue = "name") String[] sortBy,
                                                 @RequestParam(defaultValue = "ASC") String sortDirection){
        ResponseDTO res = service.findTagsByUser(pageNumber, pageSize, sortBy, sortDirection);
        return new ResponseEntity<>(res.getBody(), res.getStatus());
    }

    @PostMapping("/")
    public ResponseEntity<Object> saveTag(@RequestBody TagDTO tagDTO){
        ResponseDTO res = service.saveTag(tagDTO);
        return new ResponseEntity<>(res.getBody(), res.getStatus());
    }

    @PutMapping("/{tagId}")
    public ResponseEntity<Object> editTag(@PathVariable Long tagId, @RequestBody TagDTO tagDTO){
        ResponseDTO res = service.editTag(tagId, tagDTO);
        return new ResponseEntity<>(res.getBody(), res.getStatus());

    }

    @DeleteMapping("/{tagId}")
    public ResponseEntity<Object> deleteTag(@PathVariable Long tagId,
                                            @RequestParam(defaultValue = "false") Boolean forceDelete){
        ResponseDTO res = service.deleteTag(tagId, forceDelete);
        return new ResponseEntity<>(res.getBody(), res.getStatus());
    }
}
