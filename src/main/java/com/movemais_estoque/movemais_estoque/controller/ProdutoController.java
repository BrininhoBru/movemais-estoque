package com.movemais_estoque.movemais_estoque.controller;

import com.movemais_estoque.movemais_estoque.dto.produto.ProdutoDTO;
import com.movemais_estoque.movemais_estoque.dto.produto.ProdutoUpdateDTO;
import com.movemais_estoque.movemais_estoque.model.Produto;
import com.movemais_estoque.movemais_estoque.response.ApiResponsePattern;
import com.movemais_estoque.movemais_estoque.response.ResponseBuilder;
import com.movemais_estoque.movemais_estoque.service.ProdutoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
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

    @Operation(
            summary = "Criar produto",
            description = "Cria um novo produto",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Produto criado com sucesso",
                            content = @Content(schema = @Schema(implementation = ProdutoDTO.class))
                    ),
                    @ApiResponse(responseCode = "400", description = "Dados inválidos")
            }
    )
    @PostMapping
    public ResponseEntity<ApiResponsePattern<ProdutoDTO>> criarProduto(@RequestBody @Valid ProdutoDTO request) {
        Produto produto = modelMapper.map(request, Produto.class);
        Produto produtoCriado = produtoService.criar(produto);
        return ResponseBuilder.ok(modelMapper.map(produtoCriado, ProdutoDTO.class));
    }

    @Operation(
            summary = "Listar produtos",
            description = "Lista produtos com filtros opcionais e paginação",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Produtos listados com sucesso",
                            content = @Content(schema = @Schema(implementation = ProdutoDTO.class))
                    )
            }
    )
    @GetMapping
    public ResponseEntity<ApiResponsePattern<Page<ProdutoDTO>>> listarProdutos(
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

    @Operation(
            summary = "Atualizar produto",
            description = "Atualiza dados de um produto existente",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Produto atualizado com sucesso",
                            content = @Content(schema = @Schema(implementation = ProdutoDTO.class))
                    ),
                    @ApiResponse(responseCode = "400", description = "Dados inválidos"),
                    @ApiResponse(responseCode = "404", description = "Produto não encontrado")
            }
    )
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponsePattern<ProdutoDTO>> atualizar(
            @PathVariable Long id,
            @RequestBody @Valid ProdutoUpdateDTO request) {

        Produto produtoAtualizado = produtoService.atualizar(id, request);
        ProdutoDTO dto = modelMapper.map(produtoAtualizado, ProdutoDTO.class);
        return ResponseBuilder.ok(dto);
    }

    @Operation(
            summary = "Inativar produto",
            description = "Marca um produto como inativo",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Produto inativado com sucesso",
                            content = @Content(schema = @Schema(implementation = ProdutoDTO.class))
                    ),
                    @ApiResponse(responseCode = "404", description = "Produto não encontrado")
            }
    )
    @PatchMapping("/inativar/{id}")
    public ResponseEntity<ApiResponsePattern<ProdutoDTO>> inativarProduto(@PathVariable Long id) {
        Produto produtoAtualizado = produtoService.inativar(id);
        ProdutoDTO dto = modelMapper.map(produtoAtualizado, ProdutoDTO.class);
        return ResponseBuilder.ok(dto);
    }
}