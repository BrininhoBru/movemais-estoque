package com.movemais_estoque.movemais_estoque.controller;

import com.movemais_estoque.movemais_estoque.dto.movimento_estoque.MovimentoEstoqueRelatorioRequestDTO;
import com.movemais_estoque.movemais_estoque.dto.movimento_estoque.MovimentoEstoqueRelatorioResponseDTO;
import com.movemais_estoque.movemais_estoque.dto.movimento_estoque.MovimentoEstoqueRequestDTO;
import com.movemais_estoque.movemais_estoque.dto.movimento_estoque.MovimentoEstoqueResponseDTO;
import com.movemais_estoque.movemais_estoque.model.MovimentoEstoque;
import com.movemais_estoque.movemais_estoque.response.ApiResponsePattern;
import com.movemais_estoque.movemais_estoque.response.ResponseBuilder;
import com.movemais_estoque.movemais_estoque.security.CustomUserDetails;
import com.movemais_estoque.movemais_estoque.service.MovimentoEstoqueService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/movimentos")
@RequiredArgsConstructor
public class MovimentoEstoqueController {

    private final MovimentoEstoqueService movimentoService;
    private final ModelMapper mapper;

    @Operation(
            summary = "Registrar entrada de estoque",
            description = "Registra uma entrada de estoque para um produto em um depósito",
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "Entrada registrada com sucesso",
                            content = @Content(schema = @Schema(implementation = MovimentoEstoqueResponseDTO.class))
                    ),
                    @ApiResponse(responseCode = "400", description = "Dados inválidos")
            }
    )
    @PostMapping("/entrada")
    public ResponseEntity<ApiResponsePattern<MovimentoEstoqueResponseDTO>> registrarEntrada(
            @RequestBody @Valid MovimentoEstoqueRequestDTO dto) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        Long usuarioId = userDetails.getId();

        MovimentoEstoque mov = movimentoService.registrarEntrada(dto, usuarioId);
        return ResponseBuilder.created(mapper.map(mov, MovimentoEstoqueResponseDTO.class));
    }

    @Operation(
            summary = "Registrar saída de estoque",
            description = "Registra uma saída de estoque para um produto em um depósito",
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "Saída registrada com sucesso",
                            content = @Content(schema = @Schema(implementation = MovimentoEstoqueResponseDTO.class))
                    ),
                    @ApiResponse(responseCode = "400", description = "Dados inválidos")
            }
    )
    @PostMapping("/saida")
    public ResponseEntity<ApiResponsePattern<MovimentoEstoqueResponseDTO>> registrarSaida(
            @RequestBody @Valid MovimentoEstoqueRequestDTO dto) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        Long usuarioId = userDetails.getId();

        MovimentoEstoque mov = movimentoService.registrarSaida(dto, usuarioId);
        return ResponseBuilder.created(mapper.map(mov, MovimentoEstoqueResponseDTO.class));
    }

    @Operation(
            summary = "Gerar relatório de movimentações",
            description = "Gera um relatório de movimentações filtrado por datas, tipos, produtos e depósitos",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Relatório gerado com sucesso",
                            content = @Content(schema = @Schema(implementation = MovimentoEstoqueRelatorioResponseDTO.class))
                    )
            }
    )
    @PostMapping("/relatorio")
    public ResponseEntity<ApiResponsePattern<List<MovimentoEstoqueRelatorioResponseDTO>>> relatorio(
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