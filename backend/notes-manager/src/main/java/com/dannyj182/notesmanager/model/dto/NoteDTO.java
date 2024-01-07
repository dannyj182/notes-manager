package com.dannyj182.notesmanager.model.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter @Setter
public class NoteDTO {

    private Long noteId;
    private String title;
    private String description;
    private Long userId;
    private String status;
    private LocalDateTime modificationDate;
}
