package com.example.taskmanagement.dto;

import com.example.taskmanagement.entity.User;
import com.example.taskmanagement.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class UserDTO {
    private String username;
    private String email;
    private String password;
    private Role role;
    private String accessToken;
    private String refreshToken;

}
