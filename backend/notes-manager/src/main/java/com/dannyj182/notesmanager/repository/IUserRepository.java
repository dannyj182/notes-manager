package com.dannyj182.notesmanager.repository;

import com.dannyj182.notesmanager.model.entity.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IUserRepository extends CrudRepository<User, Long> {
    Optional<User> findByUserName(String userName);
}
