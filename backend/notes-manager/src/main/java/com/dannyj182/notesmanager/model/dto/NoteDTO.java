package com.dannyj182.notesmanager.model.dto;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class NoteDTO {

    private long noteId;
    private String title;
    private String description;
    private String status;
    private String modificationDate;
}
