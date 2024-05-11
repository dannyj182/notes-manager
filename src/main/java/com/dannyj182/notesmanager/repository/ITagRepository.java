package com.dannyj182.notesmanager.repository;

import com.dannyj182.notesmanager.model.entity.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ITagRepository extends JpaRepository<Tag, Long> {
    Page<Tag> findAllByUser_Username(String username, Pageable pageable);
}
