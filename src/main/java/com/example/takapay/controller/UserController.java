package com.example.takapay.controller;

import com.example.takapay.dto.LoginDto;
import com.example.takapay.dto.RegisterDto;
import com.example.takapay.entity.User;
import com.example.takapay.service.UserService;
import com.example.takapay.utils.JWTUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.security.Principal;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@Tag(name = "Authentication", description = "Endpoint for Login and Register user")
public class UserController {

    final private UserService service;
    final private JWTUtil jwtUtil;

    public UserController(UserService service, JWTUtil jwtUtil) {
        this.service = service;
        this.jwtUtil = jwtUtil;
    }

    @Operation(summary = "Register User", description = "Register User With User Details")
    @PostMapping("/register")
    public ResponseEntity register(@RequestBody RegisterDto request) {
        return ResponseEntity.ok(service.register(request));
    }

    @Operation(summary = "Login User", description = "Login user with Credentials")
    @PostMapping("/login")
    public ResponseEntity login(@RequestBody LoginDto request) {
        User loggedInUser = service.login(request.getUsername(), request.getPassword());
        String token = jwtUtil.generateToken(loggedInUser.getUsername());
        return ResponseEntity.ok(Map.of("token", token));
    }

    @Operation(summary = "Check Balance",description = "Check logged-in user current balance")
    @GetMapping("/balance")
    public ResponseEntity checkBalance(Principal principal) {
        BigDecimal balance = service.checkBalance(principal.getName());
        return ResponseEntity.ok(Map.of("Balance ", balance));
    }
}

