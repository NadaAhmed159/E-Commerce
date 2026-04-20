package com.ecommerce.userservice.services;

import com.ecommerce.userservice.dto.*;
import com.ecommerce.userservice.entities.User;
import com.ecommerce.userservice.exception.InvalidCredentialsException;
import com.ecommerce.userservice.repos.UserRepository;
import com.ecommerce.userservice.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;
    private final BCryptPasswordEncoder passwordEncoder;

    // ── Signup ────────────────────────────────────────────────────────────────

    public SignupSuccessResponse signup(SignupRequest request) {

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new EmailAlreadyExistsException("Account Already Exists");
        }

        User user = User.builder()
                .name(request.getName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .phone(request.getPhone())
                .role(User.Role.user)
                .build();

        User saved = userRepository.save(user);
        String token = jwtUtil.generateToken(saved);

        return SignupSuccessResponse.builder()
                .message("success")
                .user(UserResponse.builder()
                        .name(saved.getName())
                        .email(saved.getEmail())
                        .role(saved.getRole().name())
                        .build())
                .token(token)
                .build();
    }

    // ── Signin ────────────────────────────────────────────────────────────────

    public SignupSuccessResponse signin(SigninRequest request) {

        // 1. Look up user by email — same generic error to prevent user enumeration
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() ->
                    new InvalidCredentialsException("Incorrect email or password")
                );

        // 2. Verify raw password against stored BCrypt hash
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new InvalidCredentialsException("Incorrect email or password");
        }

        // 3. Generate fresh JWT
        String token = jwtUtil.generateToken(user);

        return SignupSuccessResponse.builder()
                .message("success")
                .user(UserResponse.builder()
                        .name(user.getName())
                        .email(user.getEmail())
                        .role(user.getRole().name())
                        .build())
                .token(token)
                .build();
    }
}