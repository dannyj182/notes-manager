package com.dannyj182.notesmanager.model.mapper;

import com.dannyj182.notesmanager.model.dto.NoteDTO;
import com.dannyj182.notesmanager.model.entity.Note;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@Mapper(componentModel = "spring", uses = {TagMapper.class})
public interface NoteMapper {

    @Mappings({
            @Mapping(source = "status.status", target = "status")
    })
    NoteDTO toNoteDTO(Note note);

    @Mappings({
            @Mapping(target = "user", ignore = true),
            @Mapping(target = "status", ignore = true),
            @Mapping(target = "createdDate", ignore = true),
            @Mapping(target = "modifiedDate", ignore = true)
    })
    Note toNote(NoteDTO noteDTO);
}
