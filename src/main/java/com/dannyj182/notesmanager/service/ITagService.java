package com.dannyj182.notesmanager.service;

import com.dannyj182.notesmanager.model.dto.ResponseDTO;
import com.dannyj182.notesmanager.model.dto.TagDTO;
import com.dannyj182.notesmanager.model.entity.Tag;

import java.util.List;

public interface ITagService {
    ResponseDTO findTagsByUser(Long tagId, Integer pageNumber, Integer pageSize, String[] sortBy, String sortDirection);
    ResponseDTO saveTags(List<TagDTO> tagDTOList);
    ResponseDTO editTags(List<TagDTO> tagDTOList);
    ResponseDTO deleteTags(List<Long> tagIds, Boolean forceDelete);
    List<Tag> findAllById(List<TagDTO> tagDTOList);
    boolean checkTagsForNullTagId(List<TagDTO> tags);
}
