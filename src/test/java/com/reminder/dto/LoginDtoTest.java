package com.reminder.dto;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class LoginDtoTest {

    @Test
    void testGettersAndSetters() {
        LoginDto loginDto = new LoginDto();
        String usernameOrEmail = "user@example.com";
        String password = "password123";

        loginDto.setUsernameOrEmail(usernameOrEmail);
        loginDto.setPassword(password);

        assertEquals(usernameOrEmail, loginDto.getUsernameOrEmail());
        assertEquals(password, loginDto.getPassword());
    }

    @Test
    void testEqualsSameObject() {
        LoginDto loginDto = new LoginDto();
        loginDto.setUsernameOrEmail("user@example.com");
        loginDto.setPassword("password123");

        assertEquals(loginDto, loginDto);
    }

    @Test
    void testEqualsEqualObjects() {
        LoginDto loginDto1 = new LoginDto();
        loginDto1.setUsernameOrEmail("user@example.com");
        loginDto1.setPassword("password123");

        LoginDto loginDto2 = new LoginDto();
        loginDto2.setUsernameOrEmail("user@example.com");
        loginDto2.setPassword("password123");

        assertEquals(loginDto1, loginDto2);
        assertEquals(loginDto1.hashCode(), loginDto2.hashCode());
    }

    @Test
    void testEqualsDifferentObjects() {
        LoginDto loginDto1 = new LoginDto();
        loginDto1.setUsernameOrEmail("user1@example.com");
        loginDto1.setPassword("password123");

        LoginDto loginDto2 = new LoginDto();
        loginDto2.setUsernameOrEmail("user2@example.com");
        loginDto2.setPassword("password456");

        assertNotEquals(loginDto1, loginDto2);
    }

    @Test
    void testEqualsWithNull() {
        LoginDto loginDto = new LoginDto();
        loginDto.setUsernameOrEmail("user@example.com");
        loginDto.setPassword("password123");

        assertNotEquals(loginDto, null);
    }

    @Test
    void testEqualsWithDifferentClass() {
        LoginDto loginDto = new LoginDto();
        loginDto.setUsernameOrEmail("user@example.com");
        loginDto.setPassword("password123");

        String otherObject = "not a LoginDto";

        assertNotEquals(loginDto, otherObject);
    }

    @Test
    void testToString() {
        LoginDto loginDto = new LoginDto();
        loginDto.setUsernameOrEmail("user@example.com");
        loginDto.setPassword("password123");

        String expected = "LoginDto{usernameOrEmail='user@example.com', password='password123'}";

        String actual = loginDto.toString();

        assertEquals(expected, actual);
    }

    @Test
    void testHashCode() {
        LoginDto loginDto1 = new LoginDto();
        loginDto1.setUsernameOrEmail("user@example.com");
        loginDto1.setPassword("password123");

        LoginDto loginDto2 = new LoginDto();
        loginDto2.setUsernameOrEmail("user@example.com");
        loginDto2.setPassword("password123");

        assertEquals(loginDto1.hashCode(), loginDto2.hashCode());
    }
}
