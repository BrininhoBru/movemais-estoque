package com.movemais_estoque.movemais_estoque.service;

import com.movemais_estoque.movemais_estoque.dto.AuthResponse;
import com.movemais_estoque.movemais_estoque.dto.LoginRequest;
import com.movemais_estoque.movemais_estoque.dto.RegistroRequest;
import com.movemais_estoque.movemais_estoque.dto.UsuarioDTO;
import com.movemais_estoque.movemais_estoque.model.Usuario;
import com.movemais_estoque.movemais_estoque.repository.UsuarioRepository;
import com.movemais_estoque.movemais_estoque.security.jwt.JwtUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @InjectMocks
    private AuthService authService;

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private UsuarioService usuarioService;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private JwtUtil jwtUtil;

    private UsuarioDTO usuarioDTOExemplo;

    @BeforeEach
    void setUp() {
        usuarioDTOExemplo = new UsuarioDTO();
        usuarioDTOExemplo.setNome("Bruno");
        usuarioDTOExemplo.setEmail("bruno@test.com");
    }

    @Test
    void login_comCredenciaisValidas_deveRetornarToken() {
        LoginRequest loginRequest = new LoginRequest("bruno@test.com", "123456");

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(mock(UsernamePasswordAuthenticationToken.class));

        when(usuarioService.buscarPorEmail("bruno@test.com")).thenReturn(usuarioDTOExemplo);

        when(jwtUtil.generateToken("bruno@test.com")).thenReturn("TOKEN123");

        AuthResponse response = authService.login(loginRequest);

        assertThat(response).isNotNull();
        assertThat(response.token()).isEqualTo("TOKEN123");

        verify(authenticationManager, times(1)).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(usuarioService, times(1)).buscarPorEmail("bruno@test.com");
        verify(jwtUtil, times(1)).generateToken("bruno@test.com");
    }

    @Test
    void register_comEmailNovo_deveSalvarUsuarioERetornarToken() {
        RegistroRequest registroRequest = new RegistroRequest("Bruno", "novo@test.com", "123456");

        when(usuarioService.existePorEmail("novo@test.com")).thenReturn(false);

        when(passwordEncoder.encode("123456")).thenReturn("senhaHashed");

        when(jwtUtil.generateToken("novo@test.com")).thenReturn("TOKEN123");

        AuthResponse response = authService.register(registroRequest);

        assertThat(response).isNotNull();
        assertThat(response.token()).isEqualTo("TOKEN123");

        verify(usuarioRepository, times(1)).save(any(Usuario.class));
        verify(passwordEncoder, times(1)).encode("123456");
        verify(jwtUtil, times(1)).generateToken("novo@test.com");
    }

    @Test
    void register_comEmailExistente_deveLancarExcecao() {
        RegistroRequest registroRequest = new RegistroRequest("Bruno", "existente@test.com", "123456");

        when(usuarioService.existePorEmail("existente@test.com")).thenReturn(true);

        assertThatThrownBy(() -> authService.register(registroRequest))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Usuário já existe");

        verify(usuarioRepository, never()).save(any());
        verify(passwordEncoder, never()).encode(any());
        verify(jwtUtil, never()).generateToken(any());
    }
}