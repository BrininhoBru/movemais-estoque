package com.movemais_estoque.movemais_estoque.service;

import com.movemais_estoque.movemais_estoque.dto.produto.ProdutoUpdateDTO;
import com.movemais_estoque.movemais_estoque.model.Produto;
import com.movemais_estoque.movemais_estoque.repository.ProdutoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ProdutoServiceTest {

    @InjectMocks
    private ProdutoService produtoService;

    @Mock
    private ProdutoRepository produtoRepository;

    private Produto produtoExemplo;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        produtoExemplo = Produto.builder()
                .id(1L)
                .sku("SKU001")
                .nome("Produto Teste")
                .ativo(true)
                .build();
    }

    @Test
    void criarProduto_comSkuNaoCadastrado_deveSalvar() {
        when(produtoRepository.existsBySku(produtoExemplo.getSku())).thenReturn(false);
        when(produtoRepository.save(produtoExemplo)).thenReturn(produtoExemplo);

        Produto resultado = produtoService.criar(produtoExemplo);

        assertEquals(produtoExemplo.getSku(), resultado.getSku());
        verify(produtoRepository, times(1)).save(produtoExemplo);
    }

    @Test
    void criarProduto_comSkuJaCadastrado_deveLancarExcecao() {
        when(produtoRepository.existsBySku(produtoExemplo.getSku())).thenReturn(true);

        Exception exception = assertThrows(IllegalArgumentException.class, () -> produtoService.criar(produtoExemplo));

        assertEquals("SKU já cadastrado.", exception.getMessage());
        verify(produtoRepository, never()).save(any());
    }

    @Test
    void buscarPorId_comIdExistente_deveRetornarProduto() {
        when(produtoRepository.findById(1L)).thenReturn(Optional.of(produtoExemplo));

        Produto resultado = produtoService.buscarPorId(1L);

        assertNotNull(resultado);
        assertEquals(produtoExemplo.getId(), resultado.getId());
    }

    @Test
    void buscarPorId_comIdInexistente_deveLancarExcecao() {
        when(produtoRepository.findById(2L)).thenReturn(Optional.empty());

        Exception exception = assertThrows(IllegalArgumentException.class, () -> produtoService.buscarPorId(2L));

        assertEquals("Produto não encontrado.", exception.getMessage());
    }

    @Test
    void atualizarProduto_comSkuDisponivel_deveAtualizar() {
        ProdutoUpdateDTO dto = new ProdutoUpdateDTO();
        dto.setSku("SKU002");
        dto.setNome("Produto Atualizado");
        dto.setAtivo(false);

        when(produtoRepository.findById(1L)).thenReturn(Optional.of(produtoExemplo));
        when(produtoRepository.existsBySku(dto.getSku())).thenReturn(false);
        when(produtoRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        Produto resultado = produtoService.atualizar(1L, dto);

        assertEquals("SKU002", resultado.getSku());
        assertEquals("Produto Atualizado", resultado.getNome());
        assertFalse(resultado.isAtivo());
    }

    @Test
    void atualizarProduto_comSkuJaEmUso_deveLancarExcecao() {
        ProdutoUpdateDTO dto = new ProdutoUpdateDTO();
        dto.setSku("SKU_EXISTENTE");
        dto.setNome("Produto Atualizado");
        dto.setAtivo(true);

        when(produtoRepository.findById(1L)).thenReturn(Optional.of(produtoExemplo));
        when(produtoRepository.existsBySku(dto.getSku())).thenReturn(true);

        Exception exception = assertThrows(IllegalArgumentException.class, () -> produtoService.atualizar(1L, dto));
        assertEquals("SKU já está em uso.", exception.getMessage());
    }

    @Test
    void inativarProduto_comIdExistente_deveSetarAtivoFalse() {
        when(produtoRepository.findById(1L)).thenReturn(Optional.of(produtoExemplo));
        when(produtoRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        Produto resultado = produtoService.inativar(1L);

        assertFalse(resultado.isAtivo());
    }

    @Test
    void inativarProduto_comIdInexistente_deveLancarExcecao() {
        when(produtoRepository.findById(2L)).thenReturn(Optional.empty());

        Exception exception = assertThrows(RuntimeException.class, () -> produtoService.inativar(2L));
        assertEquals("Produto não encontrado", exception.getMessage());
    }

    @Test
    void listarComFiltros_deveChamarRepository() {
        Page<Produto> page = new PageImpl<>(List.of(produtoExemplo));
        when(produtoRepository.findAll(any(Specification.class), any(Pageable.class))).thenReturn(page);

        Page<Produto> resultado = produtoService.listarComFiltros("Teste", null, true, PageRequest.of(0, 10));

        assertEquals(1, resultado.getContent().size());
        verify(produtoRepository, times(1)).findAll(any(Specification.class), any(Pageable.class));
    }
}