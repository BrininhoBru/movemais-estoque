package com.movemais_estoque.movemais_estoque.repository;

import com.movemais_estoque.movemais_estoque.model.Estoque;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public interface EstoqueRepository extends JpaRepository<Estoque, Long> {

    Optional<Estoque> findByProdutoIdAndDepositoId(Long produtoId, Long depositoId);

    boolean existsByProdutoIdAndDepositoId(Long produtoId, Long depositoId);

    List<Estoque> findAllByProdutoId(Long produtoId);
}