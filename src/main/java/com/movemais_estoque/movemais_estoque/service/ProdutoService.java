package com.movemais_estoque.movemais_estoque.service;

import com.movemais_estoque.movemais_estoque.model.Produto;
import com.movemais_estoque.movemais_estoque.repository.ProdutoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProdutoService {
    private final ProdutoRepository produtoRepository;

    public Produto criar(Produto produto) {
        if (produtoRepository.existsBySku(produto.getSku())) {
            throw new IllegalArgumentException("SKU já cadastrado.");
        }
        return produtoRepository.save(produto);
    }

    public Produto buscarPorId(Long id) {
        return produtoRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Produto não encontrado."));
    }

    public List<Produto> listar(String nome, String sku, Boolean ativo) {
        return produtoRepository.findAll().stream().filter(p -> nome == null || p.getNome().toLowerCase().contains(nome.toLowerCase())).filter(p -> sku == null || p.getSku().equalsIgnoreCase(sku)).filter(p -> ativo == null || p.isAtivo() == ativo).toList();
    }

    public Produto atualizar(Long id, Produto dados) {
        Produto produto = buscarPorId(id);

        if (!produto.getSku().equals(dados.getSku()) && produtoRepository.existsBySku(dados.getSku())) {
            throw new IllegalArgumentException("SKU já está em uso.");
        }

        produto.setSku(dados.getSku());
        produto.setNome(dados.getNome());
        produto.setAtivo(dados.isAtivo());

        return produtoRepository.save(produto);
    }

    public void deletar(Long id) {
        produtoRepository.deleteById(id);
    }
}