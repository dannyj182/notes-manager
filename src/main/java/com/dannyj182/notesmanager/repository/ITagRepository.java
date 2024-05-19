package com.dannyj182.notesmanager.repository;

import com.dannyj182.notesmanager.model.entity.Tag;
import com.dannyj182.notesmanager.model.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ITagRepository extends JpaRepository<Tag, Long> {
    Page<Tag> findAllByUser(User user, Pageable pageable);
    List<Tag> findAllByNameIn(List<String> names);
    Optional<Tag> findByTagIdAndUser(Long tagId, User user);
}
