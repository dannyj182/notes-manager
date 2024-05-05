package com.dannyj182.notesmanager.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;

@MappedSuperclass
@Getter @Setter
public class Auditable {

    @Column(columnDefinition = "DATETIME")
    @CreatedDate
    private LocalDateTime createdDate;

    @Column(columnDefinition = "DATETIME")
    @LastModifiedDate
    private LocalDateTime modifiedDate;
}
