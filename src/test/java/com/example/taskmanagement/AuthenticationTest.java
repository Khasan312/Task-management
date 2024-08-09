package com.example.taskmanagement;

import com.example.taskmanagement.controller.users.UserController;
import com.example.taskmanagement.controller.users.requests.LoginRequest;
import com.example.taskmanagement.controller.users.requests.UserRequest;
import com.example.taskmanagement.entity.User;
import com.example.taskmanagement.repository.UserRepository;
import com.example.taskmanagement.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
public class AuthenticationTest {


    @Mock
    private UserController userController;

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    private UserService userService;


    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        // Создание реального сервиса с замокированными зависимостями
        userService = new UserService(
                userRepository,
                passwordEncoder,
                null,
                null,
                null
        );
        userController = new UserController(userService);
    }


    @Test
    public void testAuthentication() {
        LoginRequest request = new LoginRequest("tima@mail", "hello");
        userController.authenticate(request);
    }

    @Test
    public void testUserRegistrationSuccess() {
        // Мокирование данных
        String email = "newuser@example.com";
        String password = "password";
        UserRequest request = new UserRequest();
        request.username = "username";
        request.email = email;
        request.password = password;
        request.confirmPassword = password;

        // Мокирование поведения репозитория и энкодера
        when(userRepository.findByEmail(email)).thenReturn(Optional.empty());
        when(passwordEncoder.encode(password)).thenReturn("encodedPassword");

        // Вызов тестируемого метода через контроллер
        userController.registerUser(request);

        // Проверка вызовов
        verify(userRepository, times(1)).save(any(User.class));
        verify(passwordEncoder, times(1)).encode(password);
    }
}
