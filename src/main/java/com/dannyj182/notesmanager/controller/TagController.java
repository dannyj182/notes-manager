package com.dannyj182.notesmanager.controller;

import com.dannyj182.notesmanager.model.dto.ResponseDTO;
import com.dannyj182.notesmanager.model.dto.TagDTO;
import com.dannyj182.notesmanager.service.ITagService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tags")
@AllArgsConstructor
public class TagController {

    private final ITagService service;

    @GetMapping(value = { "/", "/{tagId}" })
    public ResponseEntity<Object> findTagsByUser(@PathVariable(required = false) Long tagId,
                                                 @RequestParam(defaultValue = "0") Integer pageNumber,
                                                 @RequestParam(defaultValue = "5") Integer pageSize,
                                                 @RequestParam(defaultValue = "name") String[] sortBy,
                                                 @RequestParam(defaultValue = "ASC") String sortDirection){
        ResponseDTO res = service.findTagsByUser(tagId, pageNumber, pageSize, sortBy, sortDirection);
        return new ResponseEntity<>(res.getBody(), res.getStatus());
    }

    @PostMapping("/")
    public ResponseEntity<Object> saveTags(@RequestBody List<TagDTO> tagDTOList){
        ResponseDTO res = service.saveTags(tagDTOList);
        return new ResponseEntity<>(res.getBody(), res.getStatus());
    }

    @PutMapping("/")
    public ResponseEntity<Object> editTags(@RequestBody List<TagDTO> tagDTOList){
        ResponseDTO res = service.editTags(tagDTOList);
        return new ResponseEntity<>(res.getBody(), res.getStatus());

    }

    @DeleteMapping("/")
    public ResponseEntity<Object> deleteTags(@RequestBody List<Long> tagIds,
                                             @RequestParam(defaultValue = "false") Boolean forceDelete){
        ResponseDTO res = service.deleteTags(tagIds, forceDelete);
        return new ResponseEntity<>(res.getBody(), res.getStatus());
    }
}
