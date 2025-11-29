package com.movemais_estoque.movemais_estoque.integration;

import com.movemais_estoque.movemais_estoque.model.Deposito;
import com.movemais_estoque.movemais_estoque.model.Estoque;
import com.movemais_estoque.movemais_estoque.model.Produto;
import com.movemais_estoque.movemais_estoque.repository.DepositoRepository;
import com.movemais_estoque.movemais_estoque.repository.EstoqueRepository;
import com.movemais_estoque.movemais_estoque.repository.ProdutoRepository;
import com.movemais_estoque.movemais_estoque.service.EstoqueService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class EstoqueServiceIT {

    @Autowired
    private EstoqueService estoqueService;

    @Autowired
    private ProdutoRepository produtoRepository;

    @Autowired
    private DepositoRepository depositoRepository;

    @Autowired
    private EstoqueRepository estoqueRepository;

    @Test
    void consultarESalvarEstoque() {
        Produto produto = Produto.builder().sku("SKU_EST_IT").nome("Produto IT").ativo(true).build();
        produtoRepository.save(produto);

        Deposito deposito = Deposito.builder().codigo("DEP_EST_IT").nome("Depósito IT").endereco("Rua IT").ativo(true).build();
        depositoRepository.save(deposito);

        Estoque estoque = Estoque.builder().produto(produto).deposito(deposito).quantidade(100).build();
        estoqueRepository.save(estoque);

        Estoque saldo = estoqueService.consultarSaldo(produto.getId(), deposito.getId());
        assertEquals(100, saldo.getQuantidade());
    }
}
