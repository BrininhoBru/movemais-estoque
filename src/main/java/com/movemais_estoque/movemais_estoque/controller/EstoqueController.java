package com.movemais_estoque.movemais_estoque.controller;

import com.movemais_estoque.movemais_estoque.dto.EstoqueDTO;
import com.movemais_estoque.movemais_estoque.model.Estoque;
import com.movemais_estoque.movemais_estoque.response.ApiResponse;
import com.movemais_estoque.movemais_estoque.response.ResponseBuilder;
import com.movemais_estoque.movemais_estoque.service.EstoqueService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/estoques")
@RequiredArgsConstructor
public class EstoqueController {

    private final EstoqueService estoqueService;
    private final ModelMapper modelMapper;

    /**
     * Consultar o saldo de um produto em um depósito específico
     */
    @GetMapping("/saldo")
    public ResponseEntity<ApiResponse<EstoqueDTO>> consultarSaldo(
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

    @GetMapping
    public ResponseEntity<ApiResponse<Page<EstoqueDTO>>> listarEstoquesFiltrados(
            @RequestParam(required = false) Long produtoId,
            @RequestParam(required = false) Long depositoId,
            Pageable pageable
    ) {
        Page<Estoque> page = estoqueService.listarEstoquesFiltrados(produtoId, depositoId, pageable);

        Page<EstoqueDTO> dtoPage = page.map(entity ->
                modelMapper.map(entity, EstoqueDTO.class)
        );

        return ResponseBuilder.ok(dtoPage);
    }
}
