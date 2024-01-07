package com.dannyj182.notesmanager.service;

import com.dannyj182.notesmanager.model.entity.Status;

import java.util.Optional;

public interface IStatusService {
    Optional<Status> findByStatus(String status);
}
