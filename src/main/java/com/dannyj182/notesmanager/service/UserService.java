package com.dannyj182.notesmanager.service;

import com.dannyj182.notesmanager.model.entity.User;
import com.dannyj182.notesmanager.repository.IUserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UserService implements IUserService {

    private final IUserRepository repository;

    @Override
    public User findById(String userName) {
        return repository.findById(userName).orElse(null);
    }
}
