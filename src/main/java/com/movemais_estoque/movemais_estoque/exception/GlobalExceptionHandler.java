package com.movemais_estoque.movemais_estoque.exception;

import com.movemais_estoque.movemais_estoque.response.ApiResponse;
import com.movemais_estoque.movemais_estoque.response.ResponseBuilder;
import jakarta.validation.constraints.NotNull;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.NoSuchElementException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(org.springframework.security.access.AccessDeniedException.class)
    public ResponseEntity<ApiResponse<Void>> handleAccessDenied() {
        return ResponseBuilder.error("Acesso negado: token inválido ou ausente.", HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(org.springframework.security.authentication.BadCredentialsException.class)
    public ResponseEntity<ApiResponse<Void>> handleBadCredentials() {
        return ResponseBuilder.error("Credenciais inválidas.", HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(org.springframework.security.core.AuthenticationException.class)
    public ResponseEntity<ApiResponse<Void>> handleAuthentication() {
        return ResponseBuilder.error("Falha de autenticação.", HttpStatus.UNAUTHORIZED);
    }

    // 404 - Registro não encontrado
    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<ApiResponse<Void>> handleNotFound(NoSuchElementException e) {
        return ResponseBuilder.error("Registro não encontrado", HttpStatus.NOT_FOUND);
    }

    // 500 - Erros gerais
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Void>> handleGeneralException(@NotNull Exception e) {
        return ResponseBuilder.error(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
