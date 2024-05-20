package com.dannyj182.notesmanager.service;

import com.dannyj182.notesmanager.model.entity.User;
import com.dannyj182.notesmanager.repository.IUserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UserService implements IUserService, UserDetailsService {

    private final IUserRepository repository;

    @Override
    public User findByUsername() {
        return repository.findByUsername(getCurrentUser()).orElse(null);
    }

    private String getCurrentUser(){
        return SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = repository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User " + username + " not found."));
        return org.springframework.security.core.userdetails.User.builder()
                .username(user.getUsername())
                .password(user.getPassword())
                .accountLocked(user.getLocked())
                .disabled(user.getDisabled())
                .build();
    }
}
