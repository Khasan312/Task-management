package com.example.taskmanagement;

import com.example.taskmanagement.controller.users.UserController;
import com.example.taskmanagement.controller.users.requests.LoginRequest;
import com.example.taskmanagement.controller.users.requests.UserRequest;
import com.example.taskmanagement.dto.UserDTO;
import com.example.taskmanagement.entity.User;
import com.example.taskmanagement.repository.UserRepository;
import com.example.taskmanagement.service.JwtService;
import com.example.taskmanagement.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
public class UserServiceTest {

    @InjectMocks
    private UserController userController;

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private UserService userService;

    @Mock
    private JwtService jwtService;

    @Mock
    private RedisTemplate<String, Object> redisTemplate;

    @Mock
    private ValueOperations<String, Object> valueOperations;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        // Мокирование поведения RedisTemplate
        when(redisTemplate.opsForValue()).thenReturn(valueOperations);

        // Создание реального сервиса с замокированными зависимостями
        userService = new UserService(
                userRepository,
                passwordEncoder,
                jwtService,
                redisTemplate,
                null  // Возможно, здесь нужен другой мок
        );
        userController = new UserController(userService);
    }

    @Test
    public void testAuthentication() {
        // Моковые данные
        String email = "tima@mail";
        String password = "hello";
        String encodedPassword = "encodedHello";
        String jwtToken = "jwtToken";
        String refreshToken = "refreshToken";

        // Создание мокового пользователя
        User mockUser = new User();
        mockUser.setEmail(email);
        mockUser.setPassword(encodedPassword);
        mockUser.setId(1L);

        // Мокирование поведения репозитория
        when(userRepository.findByEmail(email)).thenReturn(Optional.of(mockUser));

        // Мокирование поведения PasswordEncoder
        when(passwordEncoder.matches(password, encodedPassword)).thenReturn(true);

        // Мокирование поведения JwtService
        when(jwtService.generateToken(mockUser)).thenReturn(jwtToken);
        when(jwtService.generateRefreshToken(mockUser)).thenReturn(refreshToken);

        // Мокирование поведения RedisTemplate
        doNothing().when(valueOperations).set(anyString(), anyString(), anyLong(), any(TimeUnit.class));

        // Вызов тестируемого метода
        UserDTO result = userService.authenticate(new LoginRequest(email, password));

        // Проверка вызовов
        verify(userRepository, times(1)).findByEmail(email);
        verify(passwordEncoder, times(1)).matches(password, encodedPassword);
        verify(jwtService, times(1)).generateToken(mockUser);
        verify(jwtService, times(1)).generateRefreshToken(mockUser);
        verify(valueOperations, times(1)).set(anyString(), anyString(), anyLong(), any(TimeUnit.class));

        // Проверка результата
        assertEquals(jwtToken, result.getAccessToken());
        assertEquals(refreshToken, result.getRefreshToken());
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