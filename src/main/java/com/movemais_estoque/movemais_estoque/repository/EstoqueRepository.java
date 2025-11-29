package com.movemais_estoque.movemais_estoque.repository;

import com.movemais_estoque.movemais_estoque.model.Deposito;
import com.movemais_estoque.movemais_estoque.model.Estoque;
import com.movemais_estoque.movemais_estoque.model.Produto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

public interface EstoqueRepository extends JpaRepository<Estoque, Long>, JpaSpecificationExecutor<Estoque> {

    Optional<Estoque> findByProdutoIdAndDepositoId(Long produtoId, Long depositoId);

    Optional<Estoque> findByProdutoAndDeposito(Produto produto, Deposito deposito);
}