package com.movemais_estoque.movemais_estoque.service;

import com.movemais_estoque.movemais_estoque.dto.deposito.DepositoUpdateDTO;
import com.movemais_estoque.movemais_estoque.model.Deposito;
import com.movemais_estoque.movemais_estoque.model.Produto;
import com.movemais_estoque.movemais_estoque.repository.DepositoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DepositoService {
    private final DepositoRepository depositoRepository;

    public Deposito criar(Deposito deposito) {
        if (depositoRepository.existsByCodigo(deposito.getCodigo())) {
            throw new IllegalArgumentException("Código do depósito já cadastrado.");
        }
        return depositoRepository.save(deposito);
    }

    public Page<Deposito> listarComFiltros(String nome, String endereco, Boolean ativo, Pageable pageable) {
        Specification<Deposito> spec = Specification.allOf();

        if (nome != null && !nome.isBlank()) {
            spec = spec.and((root, query, cb) ->
                    cb.like(cb.lower(root.get("nome")), "%" + nome.toLowerCase() + "%"));
        }

        if (endereco != null && !endereco.isBlank()) {
            spec = spec.and((root, query, cb) ->
                    cb.like(cb.lower(root.get("endereco")), "%" + endereco.toLowerCase() + "%"));
        }

        if (ativo != null) {
            spec = spec.and((root, query, cb) ->
                    cb.equal(root.get("ativo"), ativo));
        }

        return depositoRepository.findAll(spec, pageable);
    }

    public Deposito atualizar(Long id, DepositoUpdateDTO dados) {
        Deposito dep = buscarPorId(id);

        if (!dep.getCodigo().equals(dados.getCodigo()) && depositoRepository.existsByCodigo(dados.getCodigo())) {
            throw new IllegalArgumentException("Código do deposito já está em uso.");
        }

        dep.setCodigo(dados.getCodigo());
        dep.setNome(dados.getNome());
        dep.setEndereco(dados.getEndereco());

        return depositoRepository.save(dep);
    }

    public Deposito buscarPorId(Long id) {
        return depositoRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Depósito não encontrado."));
    }

    public Deposito inativar(Long id) {
        Deposito deposito = depositoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Deposito não encontrado"));

        deposito.setAtivo(false);

        return depositoRepository.save(deposito);
    }
}
