package com.movemais_estoque.movemais_estoque.security.service;

import com.movemais_estoque.movemais_estoque.model.Usuario;
import com.movemais_estoque.movemais_estoque.repository.UsuarioRepository;
import com.movemais_estoque.movemais_estoque.security.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UsuarioRepository usuarioRepository;

    @Override
    public UserDetails loadUserByUsername(String email) {
        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado"));
        return new CustomUserDetails(usuario);
    }
}