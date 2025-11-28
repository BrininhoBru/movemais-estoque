package com.movemais_estoque.movemais_estoque.security.jwt;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.Date;

@Component
@RequiredArgsConstructor
public class JwtUtil {

    private final JwtConfig jwtConfig;
    private final long EXPIRATION = 1000 * 60 * 60 * 24; // 24h

    private byte[] getSigningKey() {
        return jwtConfig.getSecret().getBytes(StandardCharsets.UTF_8);
    }

    public String gerarToken(String email) {
        return Jwts.builder().setSubject(email).setIssuedAt(new Date()).setExpiration(new Date(System.currentTimeMillis() + EXPIRATION)).signWith(Keys.hmacShaKeyFor(getSigningKey())).compact();
    }

    public String extrairEmail(String token) {
        return Jwts.parserBuilder().setSigningKey(Keys.hmacShaKeyFor(getSigningKey())).build().parseClaimsJws(token).getBody().getSubject();
    }

    public boolean tokenValido(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(Keys.hmacShaKeyFor(getSigningKey())).build().parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
