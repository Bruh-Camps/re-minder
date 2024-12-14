package com.reminder.security;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
class SecurityConfigTest {

    @Autowired
    private WebApplicationContext context;

    private MockMvc mockMvc;

    @Test
    void shouldReturnUnauthorizedForProtectedEndpointWithoutAuthentication() throws Exception {
        mockMvc = MockMvcBuilders.webAppContextSetup(context).build();

        mockMvc.perform(get("/api/user/items")) // Um endpoint protegido
                .andExpect(status().isUnauthorized()); // Espera 401 Unauthorized
    }

    @Test
    @WithMockUser(username = "user", roles = {"NORMAL_USER"})
    void shouldReturnForbiddenForAdminEndpointWithInsufficientRole() throws Exception {
        mockMvc = MockMvcBuilders.webAppContextSetup(context).build();

        mockMvc.perform(get("/api/admin/manage-users")) // Um endpoint restrito a ADMIN
                .andExpect(status().isForbidden()); // Espera 403 Forbidden
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void shouldAllowAccessForAdminEndpointWithCorrectRole() throws Exception {
        mockMvc = MockMvcBuilders.webAppContextSetup(context).build();

        mockMvc.perform(get("/api/admin/manage-users")) // Um endpoint restrito a ADMIN
                .andExpect(status().isOk()); // Espera 200 OK
    }

    @Test
    @WithMockUser(username = "user", roles = {"NORMAL_USER"})
    void shouldAllowAccessForNormalUserEndpoint() throws Exception {
        mockMvc = MockMvcBuilders.webAppContextSetup(context).build();

        mockMvc.perform(get("/api/user/items")) // Um endpoint acessível para NORMAL_USER
                .andExpect(status().isOk()); // Espera 200 OK
    }

    @Test
    @WithMockUser(username = "user", roles = {"NORMAL_USER"})
    void shouldDenyPostRequestWithoutCsrfToken() throws Exception {
        mockMvc = MockMvcBuilders.webAppContextSetup(context).build();

        mockMvc.perform(post("/api/user/items")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"item1\",\"changeDaysInterval\":30,\"dateLastChange\":\"01/12/2024\"}"))
                .andExpect(status().isForbidden()); // Espera 403 Forbidden por falta de CSRF
    }

    @Test
    @WithMockUser(username = "user", roles = {"NORMAL_USER"})
    void shouldAllowPostRequestWithCsrfToken() throws Exception {
        mockMvc = MockMvcBuilders.webAppContextSetup(context).build();

        mockMvc.perform(post("/api/user/items")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"item1\",\"changeDaysInterval\":30,\"dateLastChange\":\"01/12/2024\"}")
                        .with(csrf())) // Inclui o token CSRF
                .andExpect(status().isOk()); // Espera 200 OK
    }
}