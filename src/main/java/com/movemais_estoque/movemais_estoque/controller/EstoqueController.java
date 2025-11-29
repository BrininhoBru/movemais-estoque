package com.movemais_estoque.movemais_estoque.controller;

import com.movemais_estoque.movemais_estoque.dto.EstoqueDTO;
import com.movemais_estoque.movemais_estoque.model.Estoque;
import com.movemais_estoque.movemais_estoque.response.ApiResponsePattern;
import com.movemais_estoque.movemais_estoque.response.ResponseBuilder;
import com.movemais_estoque.movemais_estoque.service.EstoqueService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/estoques")
@RequiredArgsConstructor
public class EstoqueController {

    private final EstoqueService estoqueService;
    private final ModelMapper modelMapper;

    @Operation(
            summary = "Consultar saldo",
            description = "Consulta o saldo de um produto em um depósito específico",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Saldo encontrado",
                            content = @Content(schema = @Schema(implementation = EstoqueDTO.class))
                    ),
                    @ApiResponse(responseCode = "404", description = "Saldo não encontrado")
            }
    )
    @GetMapping("/saldo")
    public ResponseEntity<ApiResponsePattern<EstoqueDTO>> consultarSaldo(
            @RequestParam Long produtoId,
            @RequestParam Long depositoId
    ) {
        Estoque estoque = estoqueService.consultarSaldo(produtoId, depositoId);

        if (estoque == null) {
            return ResponseBuilder.error("Saldo não encontrado", org.springframework.http.HttpStatus.NOT_FOUND);
        }

        EstoqueDTO dto = modelMapper.map(estoque, EstoqueDTO.class);
        return ResponseBuilder.ok(dto);
    }

    @Operation(
            summary = "Listar estoques filtrados",
            description = "Lista estoques com filtros opcionais e paginação",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Lista de estoques",
                            content = @Content(schema = @Schema(implementation = EstoqueDTO.class))
                    )
            }
    )
    @GetMapping
    public ResponseEntity<ApiResponsePattern<Page<EstoqueDTO>>> listarEstoquesFiltrados(
            @RequestParam(required = false) Long produtoId,
            @RequestParam(required = false) Long depositoId,
            Pageable pageable
    ) {
        Page<Estoque> page = estoqueService.listarEstoquesFiltrados(produtoId, depositoId, pageable);
        Page<EstoqueDTO> dtoPage = page.map(entity -> modelMapper.map(entity, EstoqueDTO.class));
        return ResponseBuilder.ok(dtoPage);
    }
}
