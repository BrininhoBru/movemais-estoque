package com.movemais_estoque.movemais_estoque.controller;

import com.movemais_estoque.movemais_estoque.dto.deposito.DepositoDTO;
import com.movemais_estoque.movemais_estoque.dto.deposito.DepositoUpdateDTO;
import com.movemais_estoque.movemais_estoque.model.Deposito;
import com.movemais_estoque.movemais_estoque.response.ApiResponsePattern;
import com.movemais_estoque.movemais_estoque.response.ResponseBuilder;
import com.movemais_estoque.movemais_estoque.service.DepositoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/depositos")
@RequiredArgsConstructor
public class DepositoController {
    private final DepositoService depositoService;
    private final ModelMapper modelMapper;

    @Operation(
            summary = "Criação de depósito",
            description = "Cria um novo depósito",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Depósito criado com sucesso",
                            content = @Content(schema = @Schema(implementation = DepositoDTO.class))
                    ),
                    @ApiResponse(responseCode = "400", description = "Dados inválidos")
            }
    )
    @PostMapping
    public ResponseEntity<ApiResponsePattern<DepositoDTO>> criarDeposito(@RequestBody DepositoDTO request) {
        Deposito deposito = modelMapper.map(request, Deposito.class);
        Deposito depositoCriado = depositoService.criar(deposito);
        return ResponseBuilder.ok(modelMapper.map(depositoCriado, DepositoDTO.class));
    }

    @Operation(
            summary = "Listagem de depósitos",
            description = "Lista depósitos com filtros opcionais e paginação",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Lista de depósitos",
                            content = @Content(schema = @Schema(implementation = DepositoDTO.class))
                    )
            }
    )
    @GetMapping
    public ResponseEntity<ApiResponsePattern<Page<DepositoDTO>>> listarDepositos(
            @RequestParam(required = false) String nome,
            @RequestParam(required = false) String endereco,
            @RequestParam(required = false) Boolean ativo,
            Pageable pageable
    ) {
        Page<Deposito> pageEntities = depositoService.listarComFiltros(nome, endereco, ativo, pageable);
        Page<DepositoDTO> pageDTOs = pageEntities.map(entity -> modelMapper.map(entity, DepositoDTO.class));
        return ResponseBuilder.ok(pageDTOs);
    }

    @Operation(
            summary = "Atualizar depósito",
            description = "Atualiza dados de um depósito existente",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Depósito atualizado",
                            content = @Content(schema = @Schema(implementation = DepositoDTO.class))
                    ),
                    @ApiResponse(responseCode = "404", description = "Depósito não encontrado")
            }
    )
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponsePattern<DepositoDTO>> atualizar(
            @PathVariable Long id,
            @RequestBody DepositoUpdateDTO request) {

        Deposito depositoAtualizado = depositoService.atualizar(id, request);
        DepositoDTO dto = modelMapper.map(depositoAtualizado, DepositoDTO.class);

        return ResponseBuilder.ok(dto);
    }

    @Operation(
            summary = "Inativar depósito",
            description = "Marca um depósito como inativo",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Depósito inativado",
                            content = @Content(schema = @Schema(implementation = DepositoDTO.class))
                    ),
                    @ApiResponse(responseCode = "404", description = "Depósito não encontrado")
            }
    )
    @PatchMapping("/inativar/{id}")
    public ResponseEntity<ApiResponsePattern<DepositoDTO>> inativarDeposito(@PathVariable Long id) {
        Deposito depositoAtualizado = depositoService.inativar(id);
        DepositoDTO dto = modelMapper.map(depositoAtualizado, DepositoDTO.class);
        return ResponseBuilder.ok(dto);
    }
}
