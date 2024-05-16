package com.dannyj182.notesmanager.audit;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;

@MappedSuperclass
@Getter
@Setter
public class Auditable {

    @Column(columnDefinition = "DATETIME")
    @CreatedDate
    @JsonIgnore
    private LocalDateTime createdDate;

    @Column(columnDefinition = "DATETIME")
    @LastModifiedDate
    private LocalDateTime modifiedDate;

    @Column
    @CreatedBy
    @JsonIgnore
    private String createdBy;

    @Column
    @LastModifiedBy
    @JsonIgnore
    private String modifiedBy;
}
