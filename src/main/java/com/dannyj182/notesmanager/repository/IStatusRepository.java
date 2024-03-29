package com.dannyj182.notesmanager.repository;

import com.dannyj182.notesmanager.model.entity.Status;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IStatusRepository extends CrudRepository<Status, String> {
}
