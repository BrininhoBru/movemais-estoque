package com.movemais_estoque.movemais_estoque.integration;

import com.movemais_estoque.movemais_estoque.dto.movimento_estoque.MovimentoEstoqueRequestDTO;
import com.movemais_estoque.movemais_estoque.enums.TipoMovimentoEnum;
import com.movemais_estoque.movemais_estoque.model.Deposito;
import com.movemais_estoque.movemais_estoque.model.Produto;
import com.movemais_estoque.movemais_estoque.model.Usuario;
import com.movemais_estoque.movemais_estoque.repository.DepositoRepository;
import com.movemais_estoque.movemais_estoque.repository.ProdutoRepository;
import com.movemais_estoque.movemais_estoque.repository.UsuarioRepository;
import com.movemais_estoque.movemais_estoque.service.MovimentoEstoqueService;
import org.junit.jupiter.api.BeforeEach;
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
class MovimentoEstoqueServiceIT {

    @Autowired
    private MovimentoEstoqueService movimentoEstoqueService;

    @Autowired
    private ProdutoRepository produtoRepository;

    @Autowired
    private DepositoRepository depositoRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    private Produto produto;
    private Deposito deposito;
    private Usuario usuario;

    @BeforeEach
    void setUp() {
        produto = Produto.builder().sku("SKU_IT").nome("Produto IT").ativo(true).build();
        produto = produtoRepository.save(produto);

        deposito = Deposito.builder().codigo("DEP_IT").nome("Depósito IT").ativo(true).endereco("Rua IT").build();
        deposito = depositoRepository.save(deposito);

        usuario = Usuario.builder().nome("Usuário IT").email("usuario@it.com").senha("123456").build();
        usuario = usuarioRepository.save(usuario);
    }

    @Test
    void registrarEntrada_deveSalvarMovimento() {
        MovimentoEstoqueRequestDTO dto = new MovimentoEstoqueRequestDTO();
        dto.setProdutoId(produto.getId());
        dto.setDepositoId(deposito.getId());
        dto.setQuantidade(50);
        dto.setObservacao("Entrada teste");

        var movimento = movimentoEstoqueService.registrarEntrada(dto, usuario.getId());

        assertNotNull(movimento.getId());
        assertEquals(TipoMovimentoEnum.ENTRADA, movimento.getTipo());
        assertEquals(50, movimento.getQuantidade());
        assertEquals(produto.getId(), movimento.getProduto().getId());
        assertEquals(deposito.getId(), movimento.getDeposito().getId());
        assertEquals(usuario.getId(), movimento.getUsuarioResponsavel().getId());
    }

    @Test
    void registrarSaida_deveSalvarMovimento() {
        // Primeiro registra uma entrada para ter saldo
        MovimentoEstoqueRequestDTO dtoEntrada = new MovimentoEstoqueRequestDTO();
        dtoEntrada.setProdutoId(produto.getId());
        dtoEntrada.setDepositoId(deposito.getId());
        dtoEntrada.setQuantidade(50);
        dtoEntrada.setObservacao("Entrada teste");
        movimentoEstoqueService.registrarEntrada(dtoEntrada, usuario.getId());

        // Agora registra saída
        MovimentoEstoqueRequestDTO dtoSaida = new MovimentoEstoqueRequestDTO();
        dtoSaida.setProdutoId(produto.getId());
        dtoSaida.setDepositoId(deposito.getId());
        dtoSaida.setQuantidade(20);
        dtoSaida.setObservacao("Saída teste");

        var movimentoSaida = movimentoEstoqueService.registrarSaida(dtoSaida, usuario.getId());

        assertNotNull(movimentoSaida.getId());
        assertEquals(TipoMovimentoEnum.SAIDA, movimentoSaida.getTipo());
        assertEquals(20, movimentoSaida.getQuantidade());
    }
}
