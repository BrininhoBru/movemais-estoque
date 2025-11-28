package com.movemais_estoque.movemais_estoque.controller;

import com.movemais_estoque.movemais_estoque.dto.AuthResponse;
import com.movemais_estoque.movemais_estoque.dto.LoginRequest;
import com.movemais_estoque.movemais_estoque.dto.RegistroRequest;
import com.movemais_estoque.movemais_estoque.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest request) {
        return ResponseEntity.ok(authService.login(request));
    }

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@RequestBody RegistroRequest request) {
        return ResponseEntity.ok(authService.register(request));
    }
}
