package com.movemais_estoque.movemais_estoque.controller;

import com.movemais_estoque.movemais_estoque.dto.deposito.DepositoDTO;
import com.movemais_estoque.movemais_estoque.dto.deposito.DepositoUpdateDTO;
import com.movemais_estoque.movemais_estoque.model.Deposito;
import com.movemais_estoque.movemais_estoque.response.ApiResponse;
import com.movemais_estoque.movemais_estoque.response.ResponseBuilder;
import com.movemais_estoque.movemais_estoque.service.DepositoService;
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

    @PostMapping
    public ResponseEntity<ApiResponse<DepositoDTO>> criarDeposito(@RequestBody DepositoDTO request) {
        Deposito deposito = modelMapper.map(request, Deposito.class);
        Deposito depositoCriado = depositoService.criar(deposito);
        return ResponseBuilder.ok(modelMapper.map(depositoCriado, DepositoDTO.class));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<Page<DepositoDTO>>> listarDepositos(
            @RequestParam(required = false) String nome,
            @RequestParam(required = false) String endereco,
            @RequestParam(required = false) Boolean ativo,
            Pageable pageable
    ) {
        Page<Deposito> pageEntities = depositoService.listarComFiltros(nome, endereco, ativo, pageable);

        Page<DepositoDTO> pageDTOs = pageEntities.map(entity ->
                modelMapper.map(entity, DepositoDTO.class)
        );

        return ResponseBuilder.ok(pageDTOs);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<DepositoDTO>> atualizar(
            @PathVariable Long id,
            @RequestBody DepositoUpdateDTO request) {

        Deposito depositoAtualizado = depositoService.atualizar(id, request);
        DepositoDTO dto = modelMapper.map(depositoAtualizado, DepositoDTO.class);

        return ResponseBuilder.ok(dto);
    }

    @PatchMapping("/inativar/{id}")
    public ResponseEntity<ApiResponse<DepositoDTO>> inativarDeposito(@PathVariable Long id) {
        Deposito produtoAtualizado = depositoService.inativar(id);
        DepositoDTO dto = modelMapper.map(produtoAtualizado, DepositoDTO.class);
        return ResponseBuilder.ok(dto);
    }
}
