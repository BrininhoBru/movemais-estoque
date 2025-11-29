package com.movemais_estoque.movemais_estoque.service;

import com.movemais_estoque.movemais_estoque.model.Deposito;
import com.movemais_estoque.movemais_estoque.model.Estoque;
import com.movemais_estoque.movemais_estoque.model.Produto;
import com.movemais_estoque.movemais_estoque.repository.EstoqueRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class EstoqueServiceTest {

    @InjectMocks
    private EstoqueService estoqueService;

    @Mock
    private EstoqueRepository estoqueRepository;

    private Estoque estoqueExemplo;

    private Produto produtoExemplo;
    private Deposito depositoExemplo;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        produtoExemplo = Produto.builder()
                .id(1L)
                .sku("SKU001")
                .nome("Produto Teste")
                .ativo(true)
                .build();

        depositoExemplo = Deposito.builder()
                .id(1L)
                .codigo("DEP001")
                .nome("Depósito Teste")
                .ativo(true)
                .build();

        estoqueExemplo = Estoque.builder()
                .id(1L)
                .produto(produtoExemplo)
                .deposito(depositoExemplo)
                .quantidade(100)
                .build();
    }

    @Test
    void consultarSaldo_comProdutoEDepositoExistentes_deveRetornarEstoque() {
        when(estoqueRepository.findByProdutoIdAndDepositoId(produtoExemplo.getId(), depositoExemplo.getId()))
                .thenReturn(Optional.of(estoqueExemplo));

        Estoque resultado = estoqueService.consultarSaldo(produtoExemplo.getId(), depositoExemplo.getId());

        assertNotNull(resultado);
        assertEquals(100, resultado.getQuantidade());
        assertEquals("Produto Teste", resultado.getProduto().getNome());
        assertEquals("Depósito Teste", resultado.getDeposito().getNome());
        verify(estoqueRepository, times(1)).findByProdutoIdAndDepositoId(produtoExemplo.getId(), depositoExemplo.getId());
    }

    @Test
    void consultarSaldo_comProdutoOuDepositoInexistente_deveRetornarNull() {
        when(estoqueRepository.findByProdutoIdAndDepositoId(2L, 2L))
                .thenReturn(Optional.empty());

        Estoque resultado = estoqueService.consultarSaldo(2L, 2L);

        assertNull(resultado);
        verify(estoqueRepository, times(1)).findByProdutoIdAndDepositoId(2L, 2L);
    }

    @Test
    void listarEstoquesFiltrados_deveChamarRepository() {
        Page<Estoque> page = new PageImpl<>(List.of(estoqueExemplo));
        when(estoqueRepository.findAll(any(Specification.class), any(Pageable.class))).thenReturn(page);

        Pageable pageable = PageRequest.of(0, 10);
        Page<Estoque> resultado = estoqueService.listarEstoquesFiltrados(produtoExemplo.getId(), depositoExemplo.getId(), pageable);

        assertEquals(1, resultado.getContent().size());
        assertEquals(100, resultado.getContent().getFirst().getQuantidade());
        assertEquals("Produto Teste", resultado.getContent().getFirst().getProduto().getNome());
        assertEquals("Depósito Teste", resultado.getContent().getFirst().getDeposito().getNome());
        verify(estoqueRepository, times(1)).findAll(any(Specification.class), eq(pageable));
    }
}
