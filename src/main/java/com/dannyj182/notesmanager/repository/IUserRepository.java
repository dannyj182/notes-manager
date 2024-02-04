package com.dannyj182.notesmanager.repository;

import com.dannyj182.notesmanager.model.entity.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IUserRepository extends CrudRepository<User, String> {
}
