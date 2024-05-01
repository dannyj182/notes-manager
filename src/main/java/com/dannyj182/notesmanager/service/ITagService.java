package com.dannyj182.notesmanager.service;

import com.dannyj182.notesmanager.model.dto.TagDTO;
import com.dannyj182.notesmanager.model.entity.Tag;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface ITagService {
    TagDTO saveTag(TagDTO tagDTO);
    List<TagDTO> findTagByUsername();
    ResponseEntity<?> deleteById(String name);
    List<Tag> findAllById(List<TagDTO> tagDTOList);
}
