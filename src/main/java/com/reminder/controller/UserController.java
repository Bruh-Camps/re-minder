package com.reminder.controller;

import com.reminder.model.User;
import com.reminder.repository.UserRepository;
import com.reminder.security.JwtAuthenticationFilter;
import com.reminder.security.JwtTokenBlacklistService;
import com.reminder.security.JwtTokenProvider;
import com.reminder.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    private final UserService userService;
    private final JwtTokenBlacklistService jwtTokenBlacklistService;

    @Autowired
    public UserController(UserService userService, JwtTokenBlacklistService jwtTokenBlacklistService) {
        this.userService = userService;
        this.jwtTokenBlacklistService = jwtTokenBlacklistService;
    }

    @Autowired
    private UserRepository userRepository;

    @PostMapping("/signout")
    public ResponseEntity<?> signOutUser(HttpServletRequest request) {

        String token = jwtAuthenticationFilter.getTokenFromRequest(request);

        if (token != null && jwtTokenProvider.validateToken(token)) {
            String username = jwtTokenProvider.getUsernameFromToken(token);

            // Recupera todos os tokens associados ao usuário e os adiciona à blacklist
            List<String> userTokens = jwtTokenBlacklistService.getTokensByUsername(username);
            for (String userToken : userTokens) {
                jwtTokenBlacklistService.addTokenToBlacklist(userToken);
            }

            SecurityContextHolder.clearContext();

            return new ResponseEntity<>("User signed out successfully", HttpStatus.OK);
        }
        return new ResponseEntity<>("User is not authenticated", HttpStatus.UNAUTHORIZED);
    }

    @DeleteMapping("/remove/{username}")
    public ResponseEntity<?> removeUser(@PathVariable String username, Authentication authentication) {

        // Obtém o usuário autenticado que está realizando a operação
        User currentUser =  userService.getCurrentUser();

        // Encontra o usuário que vai ser deletado usando seu username
        Optional<User> userToBeRemoved = userRepository.findByUsername(username);
        if (userToBeRemoved.isEmpty()) {
            return new ResponseEntity<>("User not found!", HttpStatus.NOT_FOUND);
        }

        // Verifica se o usuário autenticado tem permissão para remover
        boolean isSelfRemoval = currentUser.getUsername().equals(userToBeRemoved.get().getUsername());
        boolean isAdmin = authentication.getAuthorities().stream()
                .anyMatch(authority -> authority.getAuthority().equals("ROLE_ADMIN"));
        if (!isAdmin && !isSelfRemoval) {
            return new ResponseEntity<>("You are not authorized to remove this user!", HttpStatus.FORBIDDEN);
        }

        // Remove o usuário
        userRepository.delete(userToBeRemoved.get());

        return new ResponseEntity<>("User removed successfully", HttpStatus.OK);
    }
}
