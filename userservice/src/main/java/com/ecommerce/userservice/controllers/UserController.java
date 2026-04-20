package com.ecommerce.userservice.controllers;

import com.ecommerce.userservice.dto.ChangePasswordRequest;
import com.ecommerce.userservice.dto.SignupSuccessResponse;
import com.ecommerce.userservice.services.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PutMapping("/changeMyPassword")
    public ResponseEntity<SignupSuccessResponse> changeMyPassword(
            @RequestHeader("token") String tokenHeader, // ← change this line only
            @Valid @RequestBody ChangePasswordRequest request) {

        SignupSuccessResponse response = userService.changePassword(tokenHeader, request);
        return ResponseEntity.ok(response);
    }
}