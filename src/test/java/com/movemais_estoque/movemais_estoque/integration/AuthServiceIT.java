package com.movemais_estoque.movemais_estoque.integration;

import com.movemais_estoque.movemais_estoque.dto.LoginRequest;
import com.movemais_estoque.movemais_estoque.dto.RegistroRequest;
import com.movemais_estoque.movemais_estoque.repository.UsuarioRepository;
import com.movemais_estoque.movemais_estoque.security.jwt.JwtUtil;
import com.movemais_estoque.movemais_estoque.service.AuthService;
import com.movemais_estoque.movemais_estoque.service.UsuarioService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class AuthServiceIT {

    @Autowired
    private AuthService authService;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UsuarioService usuarioService;

    @Test
    void registerELogin_deveFuncionar() {
        RegistroRequest registro = new RegistroRequest("Teste IT", "teste_it@example.com", "123456");
        var responseRegister = authService.register(registro);

        assertNotNull(responseRegister.token());

        LoginRequest login = new LoginRequest("teste_it@example.com", "123456");
        var responseLogin = authService.login(login);
        assertNotNull(responseLogin.token());
    }
}
