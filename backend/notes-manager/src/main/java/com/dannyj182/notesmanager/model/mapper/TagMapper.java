package com.dannyj182.notesmanager.model.mapper;

import com.dannyj182.notesmanager.model.dto.TagDTO;
import com.dannyj182.notesmanager.model.entity.Tag;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface TagMapper {
    TagDTO toTagDTO(Tag tag);
    List<TagDTO> toTagsDTO(List<Tag> tags);
    List<Tag> toTags(List<TagDTO> tagDTOList);
    Tag toTag(TagDTO tagDTO);
}
