package com.lestro.pricetracker.service.impl;

import com.lestro.pricetracker.dto.UserDto;
import com.lestro.pricetracker.exception.ResourceNotFoundException;
import com.lestro.pricetracker.exception.UserAlreadyExistsException;
import com.lestro.pricetracker.model.User;
import com.lestro.pricetracker.repository.UserRepository;
import com.lestro.pricetracker.service.UserService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository,
                           PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public User registerUser(UserDto userDto) {

        // check if email already exists
        if (userRepository.findByEmail(userDto.getEmail()).isPresent()) {
            throw new UserAlreadyExistsException("Email already registered");
        }

        User user = new User();
        user.setName(userDto.getName());
        user.setEmail(userDto.getEmail());
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));
        user.setActive(true);

        return userRepository.save(user);
    }

    @Override
    public User login(String email, String password) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Invalid email or password"));

        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new ResourceNotFoundException("Invalid email or password");
        }

        if (!user.isActive()) {
            throw new RuntimeException("User account is deactivated");
        }

        return user;
    }

    @Override
    public User registerOrLoginWithGoogle(UserDto userDto) {

        // If the user already exists, log them in
        return userRepository.findByEmail(userDto.getEmail())
                .orElseGet(() -> {
                    User user = new User();
                    user.setName(userDto.getName());
                    user.setEmail(userDto.getEmail());
                    user.setPassword(null); // no password for google account
                    user.setActive(true);
                    return userRepository.save(user);
                });
    }

    @Override
    public User getUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("User not found with id: " + id));
    }

    @Override
    public void deactivateUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("User not found with id: " + id));

        user.setActive(false); // soft delete

        userRepository.save(user);
    }
}