package com.dannyj182.notesmanager.service;

import com.dannyj182.notesmanager.model.entity.User;
import com.dannyj182.notesmanager.repository.IUserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UserService implements IUserService {

    private final IUserRepository repository;

    @Override
    public User findByUsername() {
        return repository.findByUsername(getCurrentUser()).orElse(null);
    }

    private String getCurrentUser(){
        return SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
    }
}
