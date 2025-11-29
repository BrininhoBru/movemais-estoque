package com.movemais_estoque.movemais_estoque.integration;

import com.movemais_estoque.movemais_estoque.dto.deposito.DepositoUpdateDTO;
import com.movemais_estoque.movemais_estoque.model.Deposito;
import com.movemais_estoque.movemais_estoque.service.DepositoService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class DepositoServiceIT {

    @Autowired
    private DepositoService depositoService;

    @Test
    void criarEBuscarDeposito() {
        Deposito deposito = Deposito.builder().codigo("DEP_IT_001").nome("Depósito IT").endereco("Rua IT").ativo(true).build();
        var salvo = depositoService.criar(deposito);
        assertNotNull(salvo.getId());

        var buscado = depositoService.buscarPorId(salvo.getId());
        assertEquals("DEP_IT_001", buscado.getCodigo());
    }

    @Test
    void atualizarDeposito() {
        Deposito deposito = Deposito.builder().codigo("DEP_IT_002").nome("Depósito IT2").endereco("Rua IT2").ativo(true).build();
        var salvo = depositoService.criar(deposito);

        DepositoUpdateDTO dto = new DepositoUpdateDTO();
        dto.setCodigo("DEP_IT_002_ATUALIZADO");
        dto.setNome("Depósito Atualizado");
        dto.setEndereco("Rua Atualizada");

        var atualizado = depositoService.atualizar(salvo.getId(), dto);
        assertEquals("DEP_IT_002_ATUALIZADO", atualizado.getCodigo());
    }
}
