package com.example.taskmanagement.controller.users.response;

import com.example.taskmanagement.dto.UserDTO;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
public class AuthResponse {
    String accessToken;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    String refreshToken;

    public static AuthResponse from(UserDTO userDTO) {
        AuthResponse response = new AuthResponse();
        response.accessToken = userDTO.getAccessToken();
        response.refreshToken = userDTO.getRefreshToken();
        return response;
    }

    public static AuthResponse refresh(UserDTO userDTO) {
        AuthResponse response = new AuthResponse();
        response.accessToken = userDTO.getAccessToken();
        return response;
    }
}
