package com.movemais_estoque.movemais_estoque.integration;

import com.movemais_estoque.movemais_estoque.dto.produto.ProdutoUpdateDTO;
import com.movemais_estoque.movemais_estoque.model.Produto;
import com.movemais_estoque.movemais_estoque.service.ProdutoService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class ProdutoServiceIT {

    @Autowired
    private ProdutoService produtoService;

    @Test
    void criarEBuscarProduto() {
        Produto produto = Produto.builder()
                .sku("SKU_IT_001")
                .nome("Produto IT")
                .ativo(true)
                .build();

        Produto salvo = produtoService.criar(produto);
        assertNotNull(salvo.getId());

        Produto buscado = produtoService.buscarPorId(salvo.getId());
        assertEquals("SKU_IT_001", buscado.getSku());
    }

    @Test
    void listarProdutosComFiltros() {
        Produto produto = Produto.builder().sku("SKU_IT_002").nome("Produto Teste").ativo(true).build();
        produtoService.criar(produto);

        var page = produtoService.listarComFiltros("Produto Teste", null, true, PageRequest.of(0, 10));
        assertEquals(1, page.getContent().size());
    }

    @Test
    void atualizarProduto() {
        Produto produto = Produto.builder().sku("SKU_IT_003").nome("Produto IT").ativo(true).build();
        Produto salvo = produtoService.criar(produto);

        ProdutoUpdateDTO dto = new ProdutoUpdateDTO();
        dto.setSku("SKU_IT_003_ATUALIZADO");
        dto.setNome("Produto Atualizado");
        dto.setAtivo(false);

        Produto atualizado = produtoService.atualizar(salvo.getId(), dto);
        assertEquals("SKU_IT_003_ATUALIZADO", atualizado.getSku());
        assertFalse(atualizado.isAtivo());
    }

    @Test
    void inativarProduto() {
        Produto produto = Produto.builder().sku("SKU_IT_004").nome("Produto IT").ativo(true).build();
        Produto salvo = produtoService.criar(produto);

        Produto inativado = produtoService.inativar(salvo.getId());
        assertFalse(inativado.isAtivo());
    }
}
