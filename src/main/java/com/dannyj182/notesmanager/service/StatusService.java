package com.dannyj182.notesmanager.service;

import com.dannyj182.notesmanager.model.entity.Status;
import com.dannyj182.notesmanager.repository.IStatusRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class StatusService implements IStatusService {

    private final IStatusRepository repository;

    @Override
    public Optional<Status> findByName(String name) {
        return repository.findByName(name);
    }
}
