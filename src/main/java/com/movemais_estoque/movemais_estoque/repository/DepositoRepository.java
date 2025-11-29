package com.movemais_estoque.movemais_estoque.repository;

import com.movemais_estoque.movemais_estoque.model.Deposito;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

public interface DepositoRepository extends JpaRepository<Deposito, Long>, JpaSpecificationExecutor<Deposito> {

    Optional<Deposito> findByCodigo(String codigo);

    boolean existsByCodigo(String codigo);
}

