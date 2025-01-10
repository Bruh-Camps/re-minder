package com.reminder.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Duration;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    @Value("${app.version:1.0.0}") // Pega a versão do application.properties (ou usa o padrão "1.0.0" se não estiver definido)
    private String appVersion;

    private final Instant startTime = Instant.now(); // Armazena o momento em que a aplicação foi iniciada

    @GetMapping("/info")
    public ResponseEntity<?> getInfo() {
        Map<String, String> response = new HashMap<>();
        response.put("version", appVersion);

        // Calcula o uptime
        Duration uptime = Duration.between(startTime, Instant.now());
        long hours = uptime.toHours();
        long minutes = uptime.toMinutes() % 60;
        long seconds = uptime.getSeconds() % 60;

        response.put("uptime", String.format("%d hours, %d minutes, %d seconds", hours, minutes, seconds));
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
