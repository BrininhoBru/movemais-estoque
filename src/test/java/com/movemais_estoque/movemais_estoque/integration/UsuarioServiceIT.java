package com.movemais_estoque.movemais_estoque.integration;

import com.movemais_estoque.movemais_estoque.dto.UsuarioDTO;
import com.movemais_estoque.movemais_estoque.model.Usuario;
import com.movemais_estoque.movemais_estoque.repository.UsuarioRepository;
import com.movemais_estoque.movemais_estoque.service.UsuarioService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class UsuarioServiceIT {

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Test
    void criarEBuscarUsuarioPorEmail() {
        Usuario usuario = Usuario.builder().nome("Usuário IT").email("usuario_it@example.com").senha("123456").build();

        usuarioRepository.save(usuario);

        UsuarioDTO dto = usuarioService.buscarPorEmail("usuario_it@example.com");
        assertEquals("Usuário IT", dto.getNome());
        assertEquals("usuario_it@example.com", dto.getEmail());
    }

    @Test
    void existePorEmail_deveRetornarTrue() {
        Usuario usuario = Usuario.builder().nome("Usuário IT2").email("usuario_it2@example.com").senha("123456").build();
        usuarioRepository.save(usuario);

        assertTrue(usuarioService.existePorEmail("usuario_it2@example.com"));
    }
}