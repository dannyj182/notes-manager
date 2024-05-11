package com.dannyj182.notesmanager.service;

import com.dannyj182.notesmanager.model.dto.ResponseDTO;
import com.dannyj182.notesmanager.model.dto.TagDTO;
import com.dannyj182.notesmanager.model.entity.Tag;
import org.springframework.data.domain.Page;

import java.util.List;

public interface ITagService {
    TagDTO saveTag(TagDTO tagDTO);
    Page<Tag> findTagByUsername(int page, int elements, String[] sortBy, String sortDirection);
    boolean deleteById(Long tagId);
    List<Tag> findAllById(List<TagDTO> tagDTOList);
    ResponseDTO editTag(Long tagId, TagDTO tagDTO);
}
