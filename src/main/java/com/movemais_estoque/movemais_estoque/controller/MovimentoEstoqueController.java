package com.movemais_estoque.movemais_estoque.controller;

import com.movemais_estoque.movemais_estoque.dto.movimento_estoque.MovimentoEstoqueRequestDTO;
import com.movemais_estoque.movemais_estoque.dto.movimento_estoque.MovimentoEstoqueResponseDTO;
import com.movemais_estoque.movemais_estoque.model.MovimentoEstoque;
import com.movemais_estoque.movemais_estoque.response.ApiResponse;
import com.movemais_estoque.movemais_estoque.response.ResponseBuilder;
import com.movemais_estoque.movemais_estoque.service.MovimentoEstoqueService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/movimentos")
@RequiredArgsConstructor
public class MovimentoEstoqueController {

    private final MovimentoEstoqueService movimentoService;
    private final ModelMapper mapper;

    @PostMapping("/entrada")
    public ResponseEntity<ApiResponse<MovimentoEstoqueResponseDTO>> registrarEntrada(
            @RequestBody MovimentoEstoqueRequestDTO dto,
            @RequestHeader("usuario-id") Long usuarioId) {

        MovimentoEstoque mov = movimentoService.registrarEntrada(dto, usuarioId);
        return ResponseBuilder.created(mapper.map(mov, MovimentoEstoqueResponseDTO.class));
    }

    @PostMapping("/saida")
    public ResponseEntity<ApiResponse<MovimentoEstoqueResponseDTO>> registrarSaida(
            @RequestBody MovimentoEstoqueRequestDTO dto,
            @RequestHeader("usuario-id") Long usuarioId) {

        MovimentoEstoque mov = movimentoService.registrarSaida(dto, usuarioId);
        return ResponseBuilder.created(mapper.map(mov, MovimentoEstoqueResponseDTO.class));
    }
}
