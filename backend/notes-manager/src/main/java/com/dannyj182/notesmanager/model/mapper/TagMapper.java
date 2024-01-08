package com.dannyj182.notesmanager.model.mapper;

import com.dannyj182.notesmanager.model.dto.TagDTO;
import com.dannyj182.notesmanager.model.entity.Tag;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

import java.util.List;

@Mapper(componentModel = "spring")
public interface TagMapper {

    TagDTO toTagDTO(Tag tag);

    List<TagDTO> toTagsDTO(List<Tag> tags);

    List<Tag> toTags(List<TagDTO> tagDTOList);

    @Mappings({
            @Mapping(target = "notes", ignore = true)
    })
    Tag toTag(TagDTO tagDTO);
}
