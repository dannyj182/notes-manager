package com.dannyj182.notesmanager.model.mapper;

import com.dannyj182.notesmanager.model.dto.NoteDTO;
import com.dannyj182.notesmanager.model.entity.Note;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

import java.util.List;

@Mapper(componentModel = "spring")
public interface NoteMapper {

    @Mappings({
            @Mapping(source = "user.userId", target = "userId"),
            @Mapping(source = "status.status", target = "status")
    })
    NoteDTO toNoteDTO(Note note);
    List<NoteDTO> toNotesDTO(List<Note> notes);
    @Mappings({
            @Mapping(target = "noteId", ignore = true),
            @Mapping(target = "user", ignore = true),
            @Mapping(target = "status", ignore = true),
            @Mapping(target = "modificationDate", ignore = true),
    })
    Note toNote(NoteDTO noteDTO);
}
