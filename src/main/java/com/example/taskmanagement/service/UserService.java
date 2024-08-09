package com.example.taskmanagement.service;

import com.example.taskmanagement.controller.users.requests.LoginRequest;
import com.example.taskmanagement.controller.users.requests.UserRequest;
import com.example.taskmanagement.dto.UserDTO;
import com.example.taskmanagement.entity.User;
import com.example.taskmanagement.enums.Role;
import com.example.taskmanagement.exception.NotFoundException;
import com.example.taskmanagement.exception.NotMatchException;
import com.example.taskmanagement.exception.PasswordMismatchException;
import com.example.taskmanagement.exception.UserAlreadyExistsException;
import com.example.taskmanagement.repository.UserRepository;

import lombok.RequiredArgsConstructor;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final RefreshTokenService refreshTokenService;
    private final RedisTemplate<String, Object> redisTemplate;


    public void saveUser(UserRequest request) {
        if (userRepository.findByEmail(request.email).isPresent()) {
            throw new UserAlreadyExistsException("User already exists");
        }

        if (!request.password.equals(request.confirmPassword)) {
            throw new PasswordMismatchException("Passwords do not match");
        }
        User user = createUser(request);
        userRepository.save(user);
    }

    public UserDTO authenticate(LoginRequest authRequest) {
        var user = userRepository.findByEmail(authRequest.email)
                .orElseThrow(() -> new NotFoundException("User not found"));

        if(!passwordEncoder.matches(authRequest.password, user.getPassword())){
            throw new NotMatchException("Вы ввели неверный пароль");
        }
        String jwtToken = jwtService.generateToken(user);
        String refreshToken = jwtService.generateRefreshToken(user);
        redisTemplate.opsForValue().set("refresh_token", refreshToken, 60, TimeUnit.MINUTES);

        return UserDTO.builder()
                .accessToken(jwtToken)
                .refreshToken(refreshToken)
                .build();
    }

    public UserDTO refresh(String refreshToken) {
        var username = jwtService.extractUsername(refreshToken);
        System.out.println(username);
        System.out.println("-----------------------");
        var storedRefreshToken = Optional.ofNullable(refreshTokenService.getRefreshToken(username))
                .orElseThrow(() -> new NotFoundException("Invalid refresh token"));

        if (!storedRefreshToken.equals(refreshToken)) {
            throw new NotMatchException("Refresh token does not match");
        }

        var user = userRepository.findByUsername(username)
                .orElseThrow(() -> new NotFoundException("User not found"));

        System.out.println(user.getUsername());
        System.out.println("------------------");
        var newAccessToken = jwtService.generateToken(user);
        var newRefreshToken = jwtService.generateRefreshToken(user);

        refreshTokenService.storeRefreshToken(user.getEmail(), newRefreshToken, jwtService.getRefreshTokenExpiration());

        return UserDTO.builder()
                .accessToken(newAccessToken)
                .refreshToken(newRefreshToken)
                .build();
    }

    public void logout(String email) {
        redisTemplate.delete(email);
    }

    private User createUser(UserRequest request) {
        return User.builder()
                .username(request.username)
                .email(request.email)
                .password(passwordEncoder.encode(request.password))
                .role(Role.USER)
                .createdAt(LocalDateTime.now().withNano(0))
                .build();
    }
}
