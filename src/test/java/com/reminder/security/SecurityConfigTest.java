package com.reminder.security;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@WebMvcTest(SecurityConfig.class)
class SecurityConfigTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @MockBean
    private UserDetailsService userDetailsService;

    @MockBean
    private CustomAuthenticationEntryPoint authenticationEntryPoint;

    @MockBean
    private CustomAccessDeniedHandler accessDeniedHandler;

    @MockBean
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Test
    void testPublicEndpointAccessibleWithoutAuthentication() throws Exception {
        mockMvc.perform(get("/api/auth/login"))
                .andExpect(status().isOk());
    }

    //TODO testAdminEndpointRequiresAdminRole

    //TODO testUserEndpointRequiresUserRole

    @Test
    public void testH2ConsoleIgnoredBySecurity() throws Exception {
        mockMvc.perform(get("/h2-console/"))
                .andExpect(status().isOk()); // Espera-se que a resposta seja 200 OK
    }

    @Test
    void testPasswordEncoderBean() {
        String password = "password123";
        String encodedPassword = passwordEncoder.encode(password);
        assertTrue(passwordEncoder.matches(password, encodedPassword));
    }

    // TODO testAuthenticationManagerBean

}