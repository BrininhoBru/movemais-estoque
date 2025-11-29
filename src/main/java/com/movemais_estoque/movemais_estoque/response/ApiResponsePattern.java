package com.movemais_estoque.movemais_estoque.response;

import lombok.Getter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Getter
public class ApiResponsePattern<T> {

    private final boolean success;
    private final String message;
    private final T data;
    private final String timestamp;

    public ApiResponsePattern(boolean success, String message, T data) {
        this.success = success;
        this.message = message;
        this.data = data;
        this.timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }
}