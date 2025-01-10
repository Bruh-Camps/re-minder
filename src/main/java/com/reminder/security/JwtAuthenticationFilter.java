package com.reminder.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final UserDetailsService userDetailsService;
    private final JwtTokenProvider jwtTokenProvider;
    private final JwtTokenBlacklistService jwtTokenBlacklistService;

    public JwtAuthenticationFilter(JwtTokenProvider jwtTokenProvider, UserDetailsService userDetailsService, JwtTokenBlacklistService jwtTokenBlacklistService) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.userDetailsService = userDetailsService;
        this.jwtTokenBlacklistService = jwtTokenBlacklistService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        // Limpa o contexto para ter que revalidar o token a cada nova requisição
        SecurityContextHolder.clearContext();

        // Extrai o token do cabeçalho Authorization
        String token = getTokenFromRequest(request);

        // Valida o token
        if (token != null && jwtTokenProvider.validateToken(token) && !jwtTokenBlacklistService.isTokenBlacklisted(token)) {

            String username = jwtTokenProvider.getUsernameFromToken(token);

            // Retorna caso o token utilizado não estiver associado ao usuário atual
            if (!jwtTokenBlacklistService.isTokenInMap(username, token)){
                //SecurityContextHolder.setContext(null);
                SecurityContextHolder.clearContext();
                return;
            }

            UserDetails userDetails = userDetailsService.loadUserByUsername(username);

            logger.warn("Username: " + username);
            logger.warn("Authorities: " + userDetails.getAuthorities());

            // Configura a autenticação no contexto do Spring Security
            UsernamePasswordAuthenticationToken authenticationToken =
                    new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

            SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        }

        // Continua com a cadeia de filtros
        filterChain.doFilter(request, response);
    }

    public String getTokenFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7); // Remove "Bearer " do início
        }
        return null;
    }
}