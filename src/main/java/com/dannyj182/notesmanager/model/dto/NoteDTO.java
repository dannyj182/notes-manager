package com.dannyj182.notesmanager.model.dto;

import lombok.Data;

import java.util.List;

@Data
public class NoteDTO {
    private long noteId;
    private String title;
    private String description;
    private String status;
    private String modificationDate;
    private List<TagDTO> tags;
}
