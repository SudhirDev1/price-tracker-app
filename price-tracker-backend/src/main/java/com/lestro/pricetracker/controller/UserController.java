package com.lestro.pricetracker.controller;

import com.lestro.pricetracker.dto.UserDto;
import com.lestro.pricetracker.model.User;
import com.lestro.pricetracker.security.JwtUtil;
import com.lestro.pricetracker.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;
    private final JwtUtil jwtUtil;

    public UserController(UserService userService, JwtUtil jwtUtil) {
        this.userService = userService;
        this.jwtUtil = jwtUtil;
    }

    // USER REGISTRATION
    @PostMapping("/register")
    public ResponseEntity<Map<String, Object>> registerUser(@RequestBody UserDto userDto) {
        User created = userService.registerUser(userDto);

        String token = jwtUtil.generateToken(created.getEmail(), created.getId());

        Map<String, Object> response = new HashMap<>();
        response.put("token", token);
        response.put("user", created);

        return ResponseEntity.ok(response);
    }

    // LOGIN
    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> login(@RequestBody UserDto userDto) {
        User loggedIn = userService.login(userDto.getEmail(), userDto.getPassword());

        String token = jwtUtil.generateToken(loggedIn.getEmail(), loggedIn.getId());

        Map<String, Object> response = new HashMap<>();
        response.put("token", token);
        response.put("user", loggedIn);

        return ResponseEntity.ok(response);
    }

    // GOOGLE LOGIN / REGISTER
    @PostMapping("/google")
    public ResponseEntity<Map<String, Object>> googleAuth(@RequestBody UserDto userDto) {
        User user = userService.registerOrLoginWithGoogle(userDto);

        String token = jwtUtil.generateToken(user.getEmail(), user.getId());

        Map<String, Object> response = new HashMap<>();
        response.put("token", token);
        response.put("user", user);

        return ResponseEntity.ok(response);
    }

    // GET USER BY ID - Protected
    @GetMapping("/{id}")
    public ResponseEntity<User> getUser(@PathVariable Long id) {
        return ResponseEntity.ok(userService.getUserById(id));
    }

    // SOFT DELETE USER - Protected
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable Long id) {
        userService.deactivateUser(id);
        return ResponseEntity.ok("User deactivated successfully.");
    }
}