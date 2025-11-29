package com.movemais_estoque.movemais_estoque.repository;

import com.movemais_estoque.movemais_estoque.model.Produto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

public interface ProdutoRepository extends JpaRepository<Produto, Long>, JpaSpecificationExecutor<Produto> {
    Optional<Produto> findBySku(String sku);

    boolean existsBySku(String sku);
}
