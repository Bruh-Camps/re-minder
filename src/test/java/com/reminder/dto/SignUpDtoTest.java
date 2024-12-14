package com.reminder.dto;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SignUpDtoTest {

    @Test
    void testGettersAndSetters() {
        SignUpDto signUpDto = new SignUpDto();
        signUpDto.setName("John Doe");
        signUpDto.setUsername("johndoe");
        signUpDto.setEmail("john.doe@example.com");
        signUpDto.setPassword("password123");

        assertEquals("John Doe", signUpDto.getName());
        assertEquals("johndoe", signUpDto.getUsername());
        assertEquals("john.doe@example.com", signUpDto.getEmail());
        assertEquals("password123", signUpDto.getPassword());
    }

    @Test
    void testEqualsSameObject() {
        SignUpDto signUpDto = new SignUpDto();
        assertEquals(signUpDto, signUpDto);
    }

    @Test
    void testEqualsDifferentObjects() {
        SignUpDto signUpDto1 = new SignUpDto();
        signUpDto1.setName("John Doe");
        signUpDto1.setUsername("johndoe");
        signUpDto1.setEmail("john.doe@example.com");
        signUpDto1.setPassword("password123");

        SignUpDto signUpDto2 = new SignUpDto();
        signUpDto2.setName("John Doe");
        signUpDto2.setUsername("johndoe");
        signUpDto2.setEmail("john.doe@example.com");
        signUpDto2.setPassword("password123");

        assertEquals(signUpDto1, signUpDto2);
    }

    @Test
    void testEqualsNotTrue() {
        SignUpDto signUpDto1 = new SignUpDto();
        signUpDto1.setName("John Doe");
        signUpDto1.setUsername("johndoe");
        signUpDto1.setEmail("john.doe@example.com");
        signUpDto1.setPassword("password123");

        SignUpDto signUpDto2 = new SignUpDto();
        signUpDto2.setName("John Marcus");
        signUpDto2.setUsername("johnmarc");
        signUpDto2.setEmail("john.marc@example.com");
        signUpDto2.setPassword("password456");

        assertNotEquals(signUpDto1, signUpDto2);
    }

    @Test
    void testEqualsNullObject() {
        SignUpDto signUpDto = new SignUpDto();
        assertNotEquals(null, signUpDto);
    }

    @Test
    void testEqualsDifferentClass() {
        SignUpDto signUpDto = new SignUpDto();
        assertNotEquals("String object", signUpDto);
    }

    @Test
    void testHashCode() {
        SignUpDto signUpDto1 = new SignUpDto();
        signUpDto1.setName("John Doe");
        signUpDto1.setUsername("johndoe");
        signUpDto1.setEmail("john.doe@example.com");
        signUpDto1.setPassword("password123");

        SignUpDto signUpDto2 = new SignUpDto();
        signUpDto2.setName("John Doe");
        signUpDto2.setUsername("johndoe");
        signUpDto2.setEmail("john.doe@example.com");
        signUpDto2.setPassword("password123");

        assertEquals(signUpDto1.hashCode(), signUpDto2.hashCode());
    }

    @Test
    void testToString() {
        SignUpDto signUpDto = new SignUpDto();
        signUpDto.setName("John Doe");
        signUpDto.setUsername("johndoe");
        signUpDto.setEmail("john.doe@example.com");
        signUpDto.setPassword("password123");

        String expected = "SignUpDto{name='John Doe', username='johndoe', email='john.doe@example.com', password='password123'}";
        assertEquals(expected, signUpDto.toString());
    }
}
