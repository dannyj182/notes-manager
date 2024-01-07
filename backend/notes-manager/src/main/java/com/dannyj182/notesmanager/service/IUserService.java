package com.dannyj182.notesmanager.service;

import com.dannyj182.notesmanager.model.entity.User;

public interface IUserService {
    User findById(long userId);
}
