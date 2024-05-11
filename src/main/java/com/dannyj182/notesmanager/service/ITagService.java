package com.dannyj182.notesmanager.service;

import com.dannyj182.notesmanager.model.dto.ResponseDTO;
import com.dannyj182.notesmanager.model.dto.TagDTO;
import com.dannyj182.notesmanager.model.entity.Tag;

import java.util.List;

public interface ITagService {
    TagDTO saveTag(TagDTO tagDTO);
    ResponseDTO findTagsByUsername(int pageNumber, int pageSize, String[] sortBy, String sortDirection);
    boolean deleteById(Long tagId);
    List<Tag> findAllById(List<TagDTO> tagDTOList);
    ResponseDTO editTag(Long tagId, TagDTO tagDTO);
    boolean checkTagsForNullTagId(List<TagDTO> tags);
}
