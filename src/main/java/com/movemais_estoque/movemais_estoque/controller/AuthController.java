package com.movemais_estoque.movemais_estoque.controller;

import com.movemais_estoque.movemais_estoque.dto.AuthResponse;
import com.movemais_estoque.movemais_estoque.dto.LoginRequest;
import com.movemais_estoque.movemais_estoque.dto.RegistroRequest;
import com.movemais_estoque.movemais_estoque.response.ApiResponsePattern;
import com.movemais_estoque.movemais_estoque.response.ResponseBuilder;
import com.movemais_estoque.movemais_estoque.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @Operation(
            summary = "Login do usuário",
            description = "Realiza login e retorna token JWT",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Login bem-sucedido",
                            content = @Content(schema = @Schema(implementation = AuthResponse.class))
                    ),
                    @ApiResponse(responseCode = "401", description = "Credenciais inválidas")
            }
    )
    @PostMapping("/login")
    public ResponseEntity<ApiResponsePattern<AuthResponse>> login(@RequestBody LoginRequest request) {
        return ResponseBuilder.ok(authService.login(request));
    }

    @Operation(
            summary = "Registro de usuário",
            description = "Registra um novo usuário e retorna token JWT",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Registro bem-sucedido",
                            content = @Content(schema = @Schema(implementation = AuthResponse.class))
                    ),
                    @ApiResponse(responseCode = "400", description = "Dados inválidos")
            }
    )
    @PostMapping("/register")
    public ResponseEntity<ApiResponsePattern<AuthResponse>> register(@RequestBody RegistroRequest request) {
        return ResponseBuilder.ok(authService.register(request));
    }
}
