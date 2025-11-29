package com.movemais_estoque.movemais_estoque.service;

import com.movemais_estoque.movemais_estoque.model.Estoque;
import com.movemais_estoque.movemais_estoque.repository.EstoqueRepository;
import com.movemais_estoque.movemais_estoque.spec.EstoqueSpecifications;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EstoqueService {

    private final EstoqueRepository estoqueRepository;

    public Estoque consultarSaldo(Long produtoId, Long depositoId) {
        return estoqueRepository.findByProdutoIdAndDepositoId(produtoId, depositoId)
                .orElse(null);
    }

    public Page<Estoque> listarEstoquesFiltrados(Long produtoId, Long depositoId, Pageable pageable) {

        var spec = org.springframework.data.jpa.domain.Specification.allOf(
                EstoqueSpecifications.hasProduto(produtoId),
                EstoqueSpecifications.hasDeposito(depositoId)
        );

        return estoqueRepository.findAll(spec, pageable);
    }
}
