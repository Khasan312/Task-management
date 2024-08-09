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
import org.springframework.web.bind.annotation.*;

import java.util.Map;

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

    @PostMapping("/refresh")
    public AuthResponse refreshToken(@RequestBody Map<String, String> refreshToken) {
        String refreshTokenValue = refreshToken.get("refreshToken");
        UserDTO userDTO = userService.refresh(refreshTokenValue);
        return AuthResponse.refresh(userDTO);
    }

}
