package com.movemais_estoque.movemais_estoque.service;

import com.movemais_estoque.movemais_estoque.model.Usuario;
import com.movemais_estoque.movemais_estoque.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;

    public Usuario criar(Usuario usuario) {

        if (usuarioRepository.existsByEmail(usuario.getEmail())) {
            throw new IllegalArgumentException("Email já cadastrado.");
        }

        return usuarioRepository.save(usuario);
    }

    public Usuario buscarPorId(Long id) {
        return usuarioRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Usuário não encontrado."));
    }

    public List<Usuario> listar() {
        return usuarioRepository.findAll();
    }

    public Usuario atualizar(Long id, Usuario dados) {
        Usuario usuario = buscarPorId(id);

        if (!usuario.getEmail().equals(dados.getEmail()) && usuarioRepository.existsByEmail(dados.getEmail())) {
            throw new IllegalArgumentException("Email já está em uso.");
        }

        usuario.setNome(dados.getNome());
        usuario.setEmail(dados.getEmail());

        if (dados.getSenha() != null) {
            usuario.setSenha(dados.getSenha());
        }

        return usuarioRepository.save(usuario);
    }

    public void deletar(Long id) {
        usuarioRepository.deleteById(id);
    }
}