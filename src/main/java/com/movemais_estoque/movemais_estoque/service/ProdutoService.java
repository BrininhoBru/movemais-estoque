package com.movemais_estoque.movemais_estoque.service;

import com.movemais_estoque.movemais_estoque.dto.produto.ProdutoUpdateDTO;
import com.movemais_estoque.movemais_estoque.model.Produto;
import com.movemais_estoque.movemais_estoque.repository.ProdutoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

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

    public Page<Produto> listarComFiltros(String nome, String sku, Boolean ativo, Pageable pageable) {
        Specification<Produto> spec = Specification.allOf();

        if (nome != null && !nome.isBlank()) {
            spec = spec.and((root, query, cb) ->
                    cb.like(cb.lower(root.get("nome")), "%" + nome.toLowerCase() + "%"));
        }

        if (sku != null && !sku.isBlank()) {
            spec = spec.and((root, query, cb) ->
                    cb.like(cb.lower(root.get("sku")), "%" + sku.toLowerCase() + "%"));
        }

        if (ativo != null) {
            spec = spec.and((root, query, cb) ->
                    cb.equal(root.get("ativo"), ativo));
        }

        return produtoRepository.findAll(spec, pageable);
    }


    public Produto atualizar(Long id, ProdutoUpdateDTO dados) {
        Produto produto = buscarPorId(id);

        if (!produto.getSku().equals(dados.getSku()) && produtoRepository.existsBySku(dados.getSku())) {
            throw new IllegalArgumentException("SKU já está em uso.");
        }

        produto.setSku(dados.getSku());
        produto.setNome(dados.getNome());
        produto.setAtivo(dados.isAtivo());

        return produtoRepository.save(produto);
    }


    public Produto inativar(Long id) {
        Produto produto = produtoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Produto não encontrado"));

        produto.setAtivo(false);

        return produtoRepository.save(produto);
    }
}