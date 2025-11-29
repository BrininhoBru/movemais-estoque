package com.movemais_estoque.movemais_estoque.response;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class ResponseBuilder {

    public static <T> ResponseEntity<ApiResponsePattern<T>> ok(T data) {
        return ResponseEntity.ok(new ApiResponsePattern<>(true, "success", data));
    }

    public static <T> ResponseEntity<ApiResponsePattern<T>> created(T data) {
        return ResponseEntity.status(HttpStatus.CREATED).body(new ApiResponsePattern<>(true, "created", data));
    }

    public static <T> ResponseEntity<ApiResponsePattern<T>> error(String message, HttpStatus status) {
        return ResponseEntity.status(status).body(new ApiResponsePattern<>(false, message, null));
    }
}