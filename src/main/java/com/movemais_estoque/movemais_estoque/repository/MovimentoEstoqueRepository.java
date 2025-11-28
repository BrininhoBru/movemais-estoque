package com.movemais_estoque.movemais_estoque.repository;

import com.movemais_estoque.movemais_estoque.model.MovimentoEstoque;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MovimentoEstoqueRepository extends JpaRepository<MovimentoEstoque, Long> {
}

