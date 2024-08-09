package com.example.taskmanagement.controller.users.requests;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class UserRequest {
    @NotBlank(message = "username не может быть пустым")
    @Size(min = 3, max = 50, message = "username должен содержать от 3 до 50 символов")
    public String username;
    @NotBlank(message = "email не может быть пустым")
    @Email(message = "email должен быть валидным")
    public String email;
    @NotBlank(message = "password не может быть пустым")
    @Size(min = 6, message = "password должен содержать не менее 6 символов")
    public String password;
    @NotBlank(message = "confirmPassword не может быть пустым")
    @Size(min = 6, message = "confirmPassword должен содержать не менее 6 символов")
    public String confirmPassword;
}
