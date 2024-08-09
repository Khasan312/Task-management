package com.example.taskmanagement.controller.users.requests;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class LoginRequest {
    @NotBlank(message = "email не может быть пустым")
    @Email(message = "email должен быть валидным")
    public String email;
    @NotBlank(message = "password не может быть пустым")
    @Size(min = 6, message = "password должен содержать не менее 6 символов")
    public String password;
}
