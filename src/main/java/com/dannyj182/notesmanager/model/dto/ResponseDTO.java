package com.dannyj182.notesmanager.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.http.HttpStatus;

@Data
@AllArgsConstructor
public class ResponseDTO {
    private Object body;
    private HttpStatus status;
}
