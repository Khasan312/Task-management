package com.example.taskmanagement.controller.users.response;

import com.example.taskmanagement.dto.UserDTO;
import lombok.Data;

@Data
public class AuthResponse {
    String accessToken;
    String refreshToken;

    public static AuthResponse from(UserDTO userDTO) {
        AuthResponse response = new AuthResponse();
        response.accessToken = userDTO.getAccessToken();
        response.refreshToken = userDTO.getRefreshToken();
        return response;
    }
}
