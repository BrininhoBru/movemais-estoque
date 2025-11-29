package com.movemais_estoque.movemais_estoque.exception;

import com.movemais_estoque.movemais_estoque.response.ApiResponsePattern;
import com.movemais_estoque.movemais_estoque.response.ResponseBuilder;
import jakarta.validation.constraints.NotNull;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(org.springframework.security.access.AccessDeniedException.class)
    public ResponseEntity<ApiResponsePattern<Void>> handleAccessDenied() {
        return ResponseBuilder.error("Acesso negado: token inválido ou ausente.", HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(org.springframework.security.authentication.BadCredentialsException.class)
    public ResponseEntity<ApiResponsePattern<Void>> handleBadCredentials() {
        return ResponseBuilder.error("Credenciais inválidas.", HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(org.springframework.security.core.AuthenticationException.class)
    public ResponseEntity<ApiResponsePattern<Void>> handleAuthentication() {
        return ResponseBuilder.error("Falha de autenticação.", HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<ApiResponsePattern<Void>> handleNotFound(NoSuchElementException e) {
        return ResponseBuilder.error("Registro não encontrado", HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponsePattern<Void>> handleGeneralException(@NotNull Exception e) {
        return ResponseBuilder.error(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponsePattern<Object>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        for (FieldError error : ex.getBindingResult().getFieldErrors()) {
            errors.put(error.getField(), error.getDefaultMessage());
        }

        return ResponseBuilder.error(errors.toString(), HttpStatus.BAD_REQUEST);
    }
}
