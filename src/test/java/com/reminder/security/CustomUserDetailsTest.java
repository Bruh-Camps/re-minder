package com.reminder.security;

import com.reminder.model.Role;
import com.reminder.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collection;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CustomUserDetailsTest {

    private User mockUser;
    private CustomUserDetails customUserDetails;

    @BeforeEach
    void setUp() {
        // Configura o mock do usuário
        mockUser = new User();
        mockUser.setUsername("testUser");
        mockUser.setPassword("testPassword");

        Role roleUser = new Role();
        roleUser.setName("USER");

        Role roleAdmin = new Role();
        roleAdmin.setName("ADMIN");

        mockUser.setRoles(Set.of(roleUser, roleAdmin));

        // Instancia o CustomUserDetails
        customUserDetails = new CustomUserDetails(mockUser);
    }

    @Test
    void testGetAuthorities_ReturnsCorrectRoles() {
        Collection<? extends GrantedAuthority> authorities = customUserDetails.getAuthorities();

        assertEquals(2, authorities.size());
        assertTrue(authorities.contains(new SimpleGrantedAuthority("ROLE_USER")));
        assertTrue(authorities.contains(new SimpleGrantedAuthority("ROLE_ADMIN")));
    }

    @Test
    void testGetPassword_ReturnsCorrectPassword() {
        assertEquals("testPassword", customUserDetails.getPassword());
    }

    @Test
    void testGetUsername_ReturnsCorrectUsername() {
        assertEquals("testUser", customUserDetails.getUsername());
    }

    @Test
    void testIsAccountNonExpired_ReturnsTrue() {
        assertTrue(customUserDetails.isAccountNonExpired());
    }

    @Test
    void testIsAccountNonLocked_ReturnsTrue() {
        assertTrue(customUserDetails.isAccountNonLocked());
    }

    @Test
    void testIsCredentialsNonExpired_ReturnsTrue() {
        assertTrue(customUserDetails.isCredentialsNonExpired());
    }

    @Test
    void testIsEnabled_ReturnsTrue() {
        assertTrue(customUserDetails.isEnabled());
    }
}
