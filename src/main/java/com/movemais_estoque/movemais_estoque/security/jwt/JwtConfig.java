package com.movemais_estoque.movemais_estoque.security.jwt;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class JwtConfig {

    @Value("${JWT_SECRET}")
    private String secret;

    public String getSecret() {
        return secret;
    }
}
