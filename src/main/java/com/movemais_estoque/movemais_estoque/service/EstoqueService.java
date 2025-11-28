package com.movemais_estoque.movemais_estoque.service;

import com.movemais_estoque.movemais_estoque.model.Estoque;
import com.movemais_estoque.movemais_estoque.repository.DepositoRepository;
import com.movemais_estoque.movemais_estoque.repository.EstoqueRepository;
import com.movemais_estoque.movemais_estoque.repository.ProdutoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EstoqueService {
    private final EstoqueRepository estoqueRepository;
    private final ProdutoRepository produtoRepository;
    private final DepositoRepository depositoRepository;

    public Estoque consultarSaldo(Long produtoId, Long depositoId) {

        return estoqueRepository.findByProdutoIdAndDepositoId(produtoId, depositoId).orElse(Estoque.builder().produto(produtoRepository.findById(produtoId).orElseThrow()).deposito(depositoRepository.findById(depositoId).orElseThrow()).quantidade(0).build());
    }

    public Integer saldoTotalProduto(Long produtoId) {
        return estoqueRepository.findAllByProdutoId(produtoId).stream().mapToInt(Estoque::getQuantidade).sum();
    }

    public Estoque salvar(Estoque estoque) {
        return estoqueRepository.save(estoque);
    }
}
