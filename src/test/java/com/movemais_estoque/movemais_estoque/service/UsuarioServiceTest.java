package com.movemais_estoque.movemais_estoque.service;

import com.movemais_estoque.movemais_estoque.dto.UsuarioDTO;
import com.movemais_estoque.movemais_estoque.model.Usuario;
import com.movemais_estoque.movemais_estoque.repository.UsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.modelmapper.ModelMapper;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UsuarioServiceTest {

    @InjectMocks
    private UsuarioService usuarioService;

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private ModelMapper modelMapper;

    private Usuario usuarioExemplo;
    private UsuarioDTO usuarioDTOExemplo;

    @BeforeEach
    void setUp() {
        usuarioExemplo = new Usuario();
        usuarioExemplo.setNome("Bruno");
        usuarioExemplo.setEmail("bruno@test.com");
        usuarioExemplo.setSenha("123456");

        usuarioDTOExemplo = new UsuarioDTO();
        usuarioDTOExemplo.setNome("Bruno");
        usuarioDTOExemplo.setEmail("bruno@test.com");
    }

    @Test
    void buscarPorEmail_quandoUsuarioExiste_deveRetornarDTO() {
        when(usuarioRepository.findByEmail("bruno@test.com")).thenReturn(Optional.of(usuarioExemplo));
        when(modelMapper.map(usuarioExemplo, UsuarioDTO.class)).thenReturn(usuarioDTOExemplo);

        UsuarioDTO resultado = usuarioService.buscarPorEmail("bruno@test.com");

        assertThat(resultado).isNotNull();
        assertThat(resultado.getEmail()).isEqualTo("bruno@test.com");
        assertThat(resultado.getNome()).isEqualTo("Bruno");

        verify(usuarioRepository, times(1)).findByEmail("bruno@test.com");
        verify(modelMapper, times(1)).map(usuarioExemplo, UsuarioDTO.class);
    }

    @Test
    void buscarPorEmail_quandoUsuarioNaoExiste_deveLancarExcecao() {
        when(usuarioRepository.findByEmail("inexistente@test.com")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> usuarioService.buscarPorEmail("inexistente@test.com")).isInstanceOf(IllegalArgumentException.class).hasMessage("Usuário não encontrado.");

        verify(usuarioRepository, times(1)).findByEmail("inexistente@test.com");
        verifyNoInteractions(modelMapper);
    }

    @Test
    void existePorEmail_quandoUsuarioExiste_deveRetornarTrue() {
        when(usuarioRepository.existsByEmail("bruno@test.com")).thenReturn(true);

        boolean resultado = usuarioService.existePorEmail("bruno@test.com");

        assertThat(resultado).isTrue();
        verify(usuarioRepository, times(1)).existsByEmail("bruno@test.com");
    }

    @Test
    void existePorEmail_quandoUsuarioNaoExiste_deveRetornarFalse() {
        when(usuarioRepository.existsByEmail("inexistente@test.com")).thenReturn(false);

        boolean resultado = usuarioService.existePorEmail("inexistente@test.com");

        assertThat(resultado).isFalse();
        verify(usuarioRepository, times(1)).existsByEmail("inexistente@test.com");
    }
}