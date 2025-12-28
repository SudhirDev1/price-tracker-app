package com.lestro.pricetracker.service;

import com.lestro.pricetracker.dto.UserDto;
import com.lestro.pricetracker.model.User;

public interface UserService {

    User registerUser(UserDto userDto);

    User login(String email, String password);

    User registerOrLoginWithGoogle(UserDto userDto);

    User getUserById(Long id);

    void deactivateUser(Long id);  // soft delete (set isActive = false)
}