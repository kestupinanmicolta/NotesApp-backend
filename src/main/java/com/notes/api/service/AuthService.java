package com.notes.api.service;

import com.notes.api.dto.AuthRequest;
import com.notes.api.dto.AuthResponse;
import com.notes.api.model.User;
import com.notes.api.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthResponse register(AuthRequest request) {
        if (request.getEmail() == null || request.getEmail().isBlank()) {
            throw new IllegalArgumentException("Email is required for registration");
        }
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new IllegalArgumentException("Username already exists");
        }
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("Email already exists");
        }

        User user = User.builder()
                .username(request.getUsername())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .build();

        userRepository.save(user);

        String token = jwtService.generateToken(user.getUsername());
        return new AuthResponse(token, user.getUsername(), "Registration successful", user.getId());
    }

    public AuthResponse login(AuthRequest request) {
        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado: " + request.getUsername()));

        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
            );
        } catch (BadCredentialsException e) {
            throw new BadCredentialsException("Contraseña incorrecta para el usuario: " + request.getUsername());
        }

        String token = jwtService.generateToken(user.getUsername());
        return new AuthResponse(token, user.getUsername(), "Login successful", user.getId());
    }
}