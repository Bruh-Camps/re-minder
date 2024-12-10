package com.reminder.controller;

import com.reminder.dto.JwtAuthResponse;
import com.reminder.dto.LoginDto;
import com.reminder.dto.SignUpDto;
import com.reminder.model.Role;
import com.reminder.model.User;
import com.reminder.repository.RoleRepository;
import com.reminder.repository.UserRepository;
import com.reminder.security.JwtTokenProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AuthControllerTest {

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private JwtTokenProvider jwtTokenProvider;

    @Mock
    private UserRepository userRepository;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private AuthController authController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        authController = new AuthController(authenticationManager, jwtTokenProvider);
        ReflectionTestUtils.setField(authController, "userRepository", userRepository);
        ReflectionTestUtils.setField(authController, "roleRepository", roleRepository);
        ReflectionTestUtils.setField(authController, "passwordEncoder", passwordEncoder);
    }

    @Test
    void testAuthenticateUser_Success() {
        // Arrange
        LoginDto loginDto = new LoginDto();
        loginDto.setUsernameOrEmail("testuser");
        loginDto.setPassword("password");

        Authentication authentication = mock(Authentication.class);
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(authentication);
        when(jwtTokenProvider.generateToken(authentication)).thenReturn("test-token");

        // Act
        ResponseEntity<?> response = authController.authenticateUser(loginDto);

        // Assert
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        JwtAuthResponse authResponse = (JwtAuthResponse) response.getBody();
        assertNotNull(authResponse);
        assertEquals("test-token", authResponse.getAccessToken());
    }

    @Test
    void testRegisterUser_Success() {
        // Arrange
        SignUpDto signUpDto = new SignUpDto();
        signUpDto.setName("Test User");
        signUpDto.setUsername("testuser");
        signUpDto.setEmail("testuser@example.com");
        signUpDto.setPassword("password");

        Role role = new Role();
        role.setName("NORMAL_USER");

        when(userRepository.existsByUsername(signUpDto.getUsername())).thenReturn(false);
        when(userRepository.existsByEmail(signUpDto.getEmail())).thenReturn(false);
        when(roleRepository.findByName("NORMAL_USER")).thenReturn(Optional.of(role));
        when(passwordEncoder.encode(signUpDto.getPassword())).thenReturn("encoded-password");

        // Act
        ResponseEntity<?> response = authController.registerUser(signUpDto);

        // Assert
        assertNotNull(response);
    }

    @Test
    void testRegisterUser_UsernameAlreadyExists() {
        // Arrange
        SignUpDto signUpDto = new SignUpDto();
        signUpDto.setUsername("testuser");

        when(userRepository.existsByUsername(signUpDto.getUsername())).thenReturn(true);

        // Act
        ResponseEntity<?> response = authController.registerUser(signUpDto);

        // Assert
        assertNotNull(response);
        assertEquals(400, response.getStatusCodeValue());
        assertEquals("Username is already taken!", response.getBody());
    }

    @Test
    void testRegisterUser_EmailAlreadyExists() {
        // Arrange
        SignUpDto signUpDto = new SignUpDto();
        signUpDto.setEmail("testuser@example.com");

        when(userRepository.existsByEmail(signUpDto.getEmail())).thenReturn(true);

        // Act
        ResponseEntity<?> response = authController.registerUser(signUpDto);

        // Assert
        assertNotNull(response);
        assertEquals(400, response.getStatusCodeValue());
        assertEquals("Email is already taken!", response.getBody());
    }

    @Test
    void testRegisterUser_RoleNotFound() {
        // Arrange
        SignUpDto signUpDto = new SignUpDto();
        signUpDto.setUsername("testuser");
        signUpDto.setEmail("testuser@example.com");

        when(userRepository.existsByUsername(signUpDto.getUsername())).thenReturn(false);
        when(userRepository.existsByEmail(signUpDto.getEmail())).thenReturn(false);
        when(roleRepository.findByName("NORMAL_USER")).thenReturn(Optional.empty());

        // Act
        ResponseEntity<?> response = authController.registerUser(signUpDto);

        // Assert
        assertNotNull(response);
        assertEquals(400, response.getStatusCodeValue());
        assertEquals("User role not found!", response.getBody());
    }
}
