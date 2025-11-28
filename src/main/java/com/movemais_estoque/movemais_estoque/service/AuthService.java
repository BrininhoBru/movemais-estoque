package com.movemais_estoque.movemais_estoque.service;

import com.movemais_estoque.movemais_estoque.dto.AuthResponse;
import com.movemais_estoque.movemais_estoque.dto.LoginRequest;
import com.movemais_estoque.movemais_estoque.dto.RegistroRequest;
import com.movemais_estoque.movemais_estoque.model.Usuario;
import com.movemais_estoque.movemais_estoque.repository.UsuarioRepository;
import com.movemais_estoque.movemais_estoque.security.jwt.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UsuarioRepository usuarioRepository;
    private final UsuarioService usuarioService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;

    public AuthResponse login(LoginRequest request) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.email(), request.senha()));

        var user = usuarioService.buscarPorEmail(request.email());

        var token = jwtUtil.generateToken(user.getEmail());
        return new AuthResponse(token);
    }

    public AuthResponse register(RegistroRequest request) {
        if (usuarioService.existePorEmail(request.email())) {
            throw new RuntimeException("Usuário já existe");
        }

        Usuario usuario = Usuario.builder()
                .nome(request.nome())
                .email(request.email())
                .senha(passwordEncoder.encode(request.senha()))
                .build();

        usuarioRepository.save(usuario);

        var token = jwtUtil.generateToken(request.email());
        return new AuthResponse(token);
    }
}

