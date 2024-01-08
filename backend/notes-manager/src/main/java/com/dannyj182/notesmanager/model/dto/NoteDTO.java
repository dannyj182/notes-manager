package com.dannyj182.notesmanager.model.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter @Setter
public class NoteDTO {

    private long noteId;
    private String title;
    private String description;
    private String status;
    private String modificationDate;
    private List<TagDTO> tags;
}
