package com.dannyj182.notesmanager.repository;

import com.dannyj182.notesmanager.model.entity.Tag;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ITagRepository extends CrudRepository<Tag, String> {
    List<Tag> findAllByUser_Username(String username);
}
