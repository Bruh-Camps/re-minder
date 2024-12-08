package com.reminder.utils;

import com.reminder.dto.JwtAuthResponse;
import com.reminder.dto.LoginDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.stereotype.Component;

@Component
public class AuthTestHelper {

    @Autowired
    private TestRestTemplate restTemplate;

    public String obtainAccessToken(String username, String password, int port) {
        String authUrl = "http://localhost:" + port + "/api/auth/signin";

        // Crie o DTO de login
        LoginDto loginDto = new LoginDto();
        loginDto.setUsernameOrEmail(username);
        loginDto.setPassword(password);

        // Configure a requisição
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<LoginDto> request = new HttpEntity<>(loginDto, headers);

        // Faça a requisição
        ResponseEntity<JwtAuthResponse> response = restTemplate.postForEntity(authUrl, request, JwtAuthResponse.class);

        // Verifique o status
        if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
            return response.getBody().getAccessToken();
        }

        throw new RuntimeException("Failed to authenticate and obtain JWT token.");
    }
}

