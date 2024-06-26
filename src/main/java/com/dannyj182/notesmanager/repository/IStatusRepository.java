package com.dannyj182.notesmanager.repository;

import com.dannyj182.notesmanager.model.entity.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IStatusRepository extends JpaRepository<Status, Long> {
    Optional<Status> findByName(String name);
}
