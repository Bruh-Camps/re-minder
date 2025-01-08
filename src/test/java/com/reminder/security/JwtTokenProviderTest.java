package com.reminder.security;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class JwtTokenProviderTest {

    private JwtTokenProvider jwtTokenProvider;

    @Mock
    private JwtTokenBlacklistService jwtTokenBlacklistService;

    @Mock
    private Authentication authentication;

    String JWTVALIDSECRET = "testetestetestetestetestetestetestetesteteste" +
            "testetestetestetestetestetestetestetestetestetestetesteteste" +
            "testetestetestetestetestetestetestetestetesteteste";

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        jwtTokenProvider = new JwtTokenProvider(jwtTokenBlacklistService);
        jwtTokenProvider.setJwtSecret(JWTVALIDSECRET);
        jwtTokenProvider.setJwtExpirationMs(1000 * 60 * 60); // 1 hora
    }

    @Test
    void testGenerateToken() {
        when(authentication.getName()).thenReturn("testUser");
        when(authentication.getAuthorities()).thenReturn(Collections.emptyList());

        String token = jwtTokenProvider.generateToken(authentication);

        assertNotNull(token);

        String username = Jwts.parser()
                .setSigningKey(jwtTokenProvider.getJwtSecret())
                .parseClaimsJws(token)
                .getBody()
                .getSubject();

        assertEquals("testUser", username);
    }

    @Test
    void testGenerateTokenWithRoles() {
        Authentication authentication = Mockito.mock(Authentication.class);

        Mockito.when(authentication.getName()).thenReturn("testuser");
        List<GrantedAuthority> authorities = List.of(
                new SimpleGrantedAuthority("ROLE_USER"),
                new SimpleGrantedAuthority("ROLE_ADMIN")
        );
        Mockito.when(authentication.getAuthorities()).thenAnswer(invocation -> authorities);

        // Gerando o token
        String token = jwtTokenProvider.generateToken(authentication);

        // Validando o token gerado
        assertNotNull(token);
        Claims claims = Jwts.parser()
                .setSigningKey(jwtTokenProvider.getJwtSecret())
                .parseClaimsJws(token)
                .getBody();

        // Verificando as informações dentro do token
        assertEquals("testuser", claims.getSubject());
        assertEquals("ROLE_USER,ROLE_ADMIN", claims.get("roles"));
    }



    @Test
    void testValidateToken() {
        String token = Jwts.builder()
                .setSubject("testUser")
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + jwtTokenProvider.getJwtExpirationMs()))
                .signWith(SignatureAlgorithm.HS512, jwtTokenProvider.getJwtSecret())
                .compact();

        assertTrue(jwtTokenProvider.validateToken(token));
    }

    @Test
    void testValidateExpiredToken() {
        String token = Jwts.builder()
                .setSubject("testUser")
                .setIssuedAt(new Date(System.currentTimeMillis() - jwtTokenProvider.getJwtExpirationMs() * 2L))
                .setExpiration(new Date(System.currentTimeMillis() - jwtTokenProvider.getJwtExpirationMs()))
                .signWith(SignatureAlgorithm.HS512, jwtTokenProvider.getJwtSecret())
                .compact();

        assertFalse(jwtTokenProvider.validateToken(token));
    }

    @Test
    void testGetUsernameFromToken() {
        String token = Jwts.builder()
                .setSubject("testUser")
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + jwtTokenProvider.getJwtExpirationMs()))
                .signWith(SignatureAlgorithm.HS512, jwtTokenProvider.getJwtSecret())
                .compact();

        String username = jwtTokenProvider.getUsernameFromToken(token);

        assertEquals("testUser", username);
    }

    @Test
    void testInvalidToken() {
        String invalidToken = "invalidToken";

        assertFalse(jwtTokenProvider.validateToken(invalidToken));
    }

    @Test
    void testEmptyClaimsToken() {
        String emptyClaimsToken = ""; // Token vazio ou inválido
        boolean isValid = jwtTokenProvider.validateToken(emptyClaimsToken);
        assertFalse(isValid, "The token with empty claims should be invalid");
    }

    @Test
    void testMalformedToken() {
        String malformedToken = "malformed-token";
        boolean isValid = jwtTokenProvider.validateToken(malformedToken);
        assertFalse(isValid, "Malformed tokens should be invalid");
    }

    @Test
    void testExpiredToken() {
        String expiredToken = Jwts.builder()
                .setSubject("testuser")
                .setExpiration(new Date(System.currentTimeMillis() - 1000)) // Expirado 1 segundo atrás
                .signWith(SignatureAlgorithm.HS512, JWTVALIDSECRET)
                .compact();

        boolean isValid = jwtTokenProvider.validateToken(expiredToken);
        assertFalse(isValid, "Expired tokens should be invalid");
    }

    @Test
    void testUnsupportedToken() throws Exception {
        // Gerar uma chave RSA para assinar o token
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
        keyPairGenerator.initialize(2048); // Tamanho da chave
        KeyPair keyPair = keyPairGenerator.generateKeyPair();
        PrivateKey privateKey = keyPair.getPrivate();

        // Criar um token com algoritmo não suportado
        String unsupportedToken = Jwts.builder()
                .setSubject("testuser")
                .signWith(SignatureAlgorithm.PS256, privateKey) // Algoritmo PS256
                .compact();

        // Validar o token
        boolean isValid = jwtTokenProvider.validateToken(unsupportedToken);
        assertFalse(isValid, "Tokens with unsupported algorithms should be invalid");
    }

    @Test
    void testValidToken() {
        String validToken = Jwts.builder()
                .setSubject("testuser")
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60)) // Expira em 1 hora
                .signWith(SignatureAlgorithm.HS512, JWTVALIDSECRET)
                .compact();

        boolean isValid = jwtTokenProvider.validateToken(validToken);
        assertTrue(isValid, "Valid tokens should be accepted");
    }

}
