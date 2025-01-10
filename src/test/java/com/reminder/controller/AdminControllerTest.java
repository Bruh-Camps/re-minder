package com.reminder.controller;

import com.reminder.dto.ItemDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

public class AdminControllerTest {


    private final  AdminController adminController = new AdminController();

    @Test
    void testControllerToGetSystemInfo() {
        // Act
        ResponseEntity<?> response = adminController.getInfo();

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }
}
