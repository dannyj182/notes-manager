package com.dannyj182.notesmanager.model.mapper;

import com.dannyj182.notesmanager.model.dto.NoteDTO;
import com.dannyj182.notesmanager.model.entity.Note;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

import java.util.List;

@Mapper(componentModel = "spring", uses = {TagMapper.class})
public interface NoteMapper {

    @Mappings({
            @Mapping(source = "status.name", target = "status")
    })
    NoteDTO toNoteDTO(Note note);

    List<NoteDTO> toNotesDTO(List<Note> notes);
}
