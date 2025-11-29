package com.movemais_estoque.movemais_estoque.security.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.movemais_estoque.movemais_estoque.response.ApiResponse;
import com.movemais_estoque.movemais_estoque.security.service.CustomUserDetailsService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final CustomUserDetailsService customUserDetailsService;

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {

        String token = jwtUtil.getToken(request);

        try {
            if (token != null && jwtUtil.validateToken(token)) {

                String email = jwtUtil.extractUsername(token);

                UserDetails userDetails =
                        customUserDetailsService.loadUserByUsername(email); // <-- aqui antes quebrava

                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(
                                userDetails,
                                null,
                                userDetails.getAuthorities()
                        );

                SecurityContextHolder.getContext().setAuthentication(authentication);
                log.info("Usuário autenticado pelo token: {}", email);
            }

            filterChain.doFilter(request, response);
        } catch (UsernameNotFoundException e) {
            log.error("Usuário não encontrado no token");
            sendError(response, "Usuário não encontrado", HttpStatus.UNAUTHORIZED);
        } catch (Exception e) {
            log.error("Erro ao validar token: {}", e.getMessage());
            sendError(response, "Token inválido ou expirado", HttpStatus.UNAUTHORIZED);
        }
    }

    private void sendError(HttpServletResponse response, String message, HttpStatus status) throws IOException {
        var body = new ApiResponse<>(false, message, null);

        response.setStatus(status.value());
        response.setContentType("application/json");
        response.getWriter().write(new ObjectMapper().writeValueAsString(body));
    }
}
