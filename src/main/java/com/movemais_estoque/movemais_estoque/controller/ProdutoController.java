package com.movemais_estoque.movemais_estoque.controller;

import com.movemais_estoque.movemais_estoque.dto.produto.ProdutoDTO;
import com.movemais_estoque.movemais_estoque.dto.produto.ProdutoUpdateDTO;
import com.movemais_estoque.movemais_estoque.model.Produto;
import com.movemais_estoque.movemais_estoque.response.ApiResponse;
import com.movemais_estoque.movemais_estoque.response.ResponseBuilder;
import com.movemais_estoque.movemais_estoque.service.ProdutoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/produtos")
@RequiredArgsConstructor
public class ProdutoController {
    private final ProdutoService produtoService;
    private final ModelMapper modelMapper;

    @PostMapping
    public ResponseEntity<ApiResponse<ProdutoDTO>> criarProduto(@RequestBody @Valid ProdutoDTO request) {
        Produto produto = modelMapper.map(request, Produto.class);
        Produto produtoCriado = produtoService.criar(produto);
        return ResponseBuilder.ok(modelMapper.map(produtoCriado, ProdutoDTO.class));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<Page<ProdutoDTO>>> listarProdutos(
            @RequestParam(required = false) String nome,
            @RequestParam(required = false) String sku,
            @RequestParam(required = false) Boolean ativo,
            Pageable pageable
    ) {
        Page<Produto> pageEntities = produtoService.listarComFiltros(nome, sku, ativo, pageable);

        Page<ProdutoDTO> pageDTOs = pageEntities.map(entity ->
                modelMapper.map(entity, ProdutoDTO.class)
        );

        return ResponseBuilder.ok(pageDTOs);
    }


    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<ProdutoDTO>> atualizar(
            @PathVariable Long id,
            @RequestBody @Valid ProdutoUpdateDTO request) {

        Produto produtoAtualizado = produtoService.atualizar(id, request);
        ProdutoDTO dto = modelMapper.map(produtoAtualizado, ProdutoDTO.class);

        return ResponseBuilder.ok(dto);
    }

    @PatchMapping("/inativar/{id}")
    public ResponseEntity<ApiResponse<ProdutoDTO>> inativarProduto(@PathVariable Long id) {
        Produto produtoAtualizado = produtoService.inativar(id);
        ProdutoDTO dto = modelMapper.map(produtoAtualizado, ProdutoDTO.class);
        return ResponseBuilder.ok(dto);
    }
}