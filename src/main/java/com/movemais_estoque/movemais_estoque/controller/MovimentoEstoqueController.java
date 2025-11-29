package com.movemais_estoque.movemais_estoque.controller;

import com.movemais_estoque.movemais_estoque.dto.movimento_estoque.MovimentoEstoqueRelatorioRequestDTO;
import com.movemais_estoque.movemais_estoque.dto.movimento_estoque.MovimentoEstoqueRelatorioResponseDTO;
import com.movemais_estoque.movemais_estoque.dto.movimento_estoque.MovimentoEstoqueRequestDTO;
import com.movemais_estoque.movemais_estoque.dto.movimento_estoque.MovimentoEstoqueResponseDTO;
import com.movemais_estoque.movemais_estoque.model.MovimentoEstoque;
import com.movemais_estoque.movemais_estoque.response.ApiResponse;
import com.movemais_estoque.movemais_estoque.response.ResponseBuilder;
import com.movemais_estoque.movemais_estoque.security.CustomUserDetails;
import com.movemais_estoque.movemais_estoque.service.MovimentoEstoqueService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/movimentos")
@RequiredArgsConstructor
public class MovimentoEstoqueController {

    private final MovimentoEstoqueService movimentoService;
    private final ModelMapper mapper;

    @PostMapping("/entrada")
    public ResponseEntity<ApiResponse<MovimentoEstoqueResponseDTO>> registrarEntrada(
            @RequestBody @Valid MovimentoEstoqueRequestDTO dto) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        Long usuarioId = userDetails.getId();

        MovimentoEstoque mov = movimentoService.registrarEntrada(dto, usuarioId);
        return ResponseBuilder.created(mapper.map(mov, MovimentoEstoqueResponseDTO.class));
    }

    @PostMapping("/saida")
    public ResponseEntity<ApiResponse<MovimentoEstoqueResponseDTO>> registrarSaida(
            @RequestBody @Valid MovimentoEstoqueRequestDTO dto) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        Long usuarioId = userDetails.getId();

        MovimentoEstoque mov = movimentoService.registrarSaida(dto, usuarioId);
        return ResponseBuilder.created(mapper.map(mov, MovimentoEstoqueResponseDTO.class));
    }

    @PostMapping("/relatorio")
    public ResponseEntity<ApiResponse<List<MovimentoEstoqueRelatorioResponseDTO>>> relatorio(
            @RequestBody MovimentoEstoqueRelatorioRequestDTO filtro) {

        List<MovimentoEstoque> movimentos = movimentoService.gerarRelatorio(filtro);

        List<MovimentoEstoqueRelatorioResponseDTO> dtos = movimentos.stream()
                .map(mov -> MovimentoEstoqueRelatorioResponseDTO
                        .builder()
                        .id(mov.getId())
                        .tipo(mov.getTipo())
                        .produtoNome(mov.getProduto().getNome())
                        .depositoNome(mov.getDeposito().getNome())
                        .quantidade(mov.getQuantidade())
                        .dataHoraMovimento(mov.getDataHoraMovimento())
                        .observacao(mov.getObservacao())
                        .usuarioResponsavel(mov.getUsuarioResponsavel().getNome())
                        .build())
                .toList();

        return ResponseBuilder.ok(dtos);
    }
}
