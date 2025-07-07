package com.Projekt.RaspberryCloud.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import com.Projekt.RaspberryCloud.dto.request.LoginUserDto;
import com.Projekt.RaspberryCloud.model.User;
import com.Projekt.RaspberryCloud.repository.UserRepository;

public class AuthenticationServiceTest {

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private AuthenticationService authenticationService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void authenticate_shouldReturnUser_whenCredentialsAreValid() {
        // Arrange
        LoginUserDto loginUserDto = new LoginUserDto();
        loginUserDto.setUsername("testuser");
        loginUserDto.setPassword("password");

        User fakeUser = new User();
        fakeUser.setUsername("testuser");

        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(fakeUser));

        // Act
        User result = authenticationService.authenticate(loginUserDto);

        // Assert
        verify(authenticationManager).authenticate(new UsernamePasswordAuthenticationToken("testuser", "password"));
        assertEquals("testuser", result.getUsername());
    }

    @Test
    void authenticate_shouldThore_whenUserNotFound() {
        // Arrange
        LoginUserDto loginUserDto = new LoginUserDto();
        loginUserDto.setUsername("nonexistent");
        loginUserDto.setPassword("password");

        when(userRepository.findByUsername("nonexistent")).thenReturn(Optional.empty());

        // Act + Assert
        assertThrows(java.util.NoSuchElementException.class,
                () -> authenticationService.authenticate(loginUserDto));
    }

}
