package com.example.taskmanagement.controller.users;

import com.example.taskmanagement.controller.users.requests.LoginRequest;
import com.example.taskmanagement.controller.users.requests.UserRequest;
import com.example.taskmanagement.controller.users.response.AuthResponse;
import com.example.taskmanagement.dto.UserDTO;
import com.example.taskmanagement.exception.NotFoundException;
import com.example.taskmanagement.exception.NotMatchException;
import com.example.taskmanagement.service.UserService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/auth")
public class UserController {
    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@Valid @RequestBody UserRequest request) {
        userService.saveUser(request);
        return ResponseEntity.ok("User registered successfully");
    }

    @PostMapping("/login")
    public AuthResponse authenticate(@Valid @RequestBody LoginRequest authRequest) throws NotFoundException, NotMatchException {
        UserDTO userDTO =  userService.authenticate(authRequest);
        return AuthResponse.from(userDTO);
    }

    @PostMapping("/logout")
    public String logout(@RequestBody String email) {
        userService.logout(email);
        return "User logged out successfully";
    }

    @PostMapping("/refresh")
    public AuthResponse refreshToken(@RequestBody String refreshToken) {
        UserDTO userDTO = userService.refresh(refreshToken);
        return AuthResponse.from(userDTO);
    }

}
