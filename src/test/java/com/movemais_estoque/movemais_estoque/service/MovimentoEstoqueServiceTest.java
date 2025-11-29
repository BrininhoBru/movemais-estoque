package com.movemais_estoque.movemais_estoque.service;

import com.movemais_estoque.movemais_estoque.dto.movimento_estoque.MovimentoEstoqueRelatorioRequestDTO;
import com.movemais_estoque.movemais_estoque.dto.movimento_estoque.MovimentoEstoqueRequestDTO;
import com.movemais_estoque.movemais_estoque.enums.TipoMovimentoEnum;
import com.movemais_estoque.movemais_estoque.model.*;
import com.movemais_estoque.movemais_estoque.repository.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class MovimentoEstoqueServiceTest {

    @InjectMocks
    private MovimentoEstoqueService movimentoService;

    @Mock
    private MovimentoEstoqueRepository movimentoEstoqueRepository;

    @Mock
    private EstoqueRepository estoqueRepository;

    @Mock
    private ProdutoRepository produtoRepository;

    @Mock
    private DepositoRepository depositoRepository;

    @Mock
    private UsuarioRepository usuarioRepository;

    private Produto produto;
    private Deposito deposito;
    private Usuario usuario;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        produto = Produto.builder().id(1L).nome("Produto").sku("SKU001").ativo(true).build();
        deposito = Deposito.builder().id(1L).nome("Depósito").codigo("DEP001").ativo(true).build();
        usuario = Usuario.builder().id(1L).nome("Usuário").email("teste@email.com").build();
    }

    @Test
    void registrarEntrada_deveSalvarMovimento() {
        MovimentoEstoqueRequestDTO dto = new MovimentoEstoqueRequestDTO();
        dto.setProdutoId(produto.getId());
        dto.setDepositoId(deposito.getId());
        dto.setQuantidade(10);
        dto.setObservacao("Entrada teste");

        when(produtoRepository.findById(produto.getId())).thenReturn(Optional.of(produto));
        when(depositoRepository.findById(deposito.getId())).thenReturn(Optional.of(deposito));
        when(usuarioRepository.findById(usuario.getId())).thenReturn(Optional.of(usuario));
        when(estoqueRepository.findByProdutoAndDeposito(produto, deposito)).thenReturn(Optional.empty());
        when(estoqueRepository.save(any(Estoque.class))).thenAnswer(i -> i.getArgument(0));
        when(movimentoEstoqueRepository.save(any(MovimentoEstoque.class))).thenAnswer(i -> i.getArgument(0));

        MovimentoEstoque resultado = movimentoService.registrarEntrada(dto, usuario.getId());

        assertNotNull(resultado);
        assertEquals(TipoMovimentoEnum.ENTRADA, resultado.getTipo());
        assertEquals(10, resultado.getQuantidade());
        assertEquals("Entrada teste", resultado.getObservacao());
        assertEquals(produto, resultado.getProduto());
        assertEquals(deposito, resultado.getDeposito());
        assertEquals(usuario, resultado.getUsuarioResponsavel());
    }

    @Test
    void registrarSaida_comSaldoSuficiente_deveSalvarMovimento() {
        MovimentoEstoqueRequestDTO dto = new MovimentoEstoqueRequestDTO();
        dto.setProdutoId(produto.getId());
        dto.setDepositoId(deposito.getId());
        dto.setQuantidade(5);
        dto.setObservacao("Saída teste");

        Estoque estoque = Estoque.builder().produto(produto).deposito(deposito).quantidade(10).build();

        when(produtoRepository.findById(produto.getId())).thenReturn(Optional.of(produto));
        when(depositoRepository.findById(deposito.getId())).thenReturn(Optional.of(deposito));
        when(usuarioRepository.findById(usuario.getId())).thenReturn(Optional.of(usuario));
        when(estoqueRepository.findByProdutoAndDeposito(produto, deposito)).thenReturn(Optional.of(estoque));
        when(estoqueRepository.save(any(Estoque.class))).thenAnswer(i -> i.getArgument(0));
        when(movimentoEstoqueRepository.save(any(MovimentoEstoque.class))).thenAnswer(i -> i.getArgument(0));

        MovimentoEstoque resultado = movimentoService.registrarSaida(dto, usuario.getId());

        assertNotNull(resultado);
        assertEquals(TipoMovimentoEnum.SAIDA, resultado.getTipo());
        assertEquals(5, resultado.getQuantidade());
        assertEquals(5, estoque.getQuantidade()); // quantidade subtraída
    }

    @Test
    void registrarSaida_comSaldoInsuficiente_deveLancarExcecao() {
        MovimentoEstoqueRequestDTO dto = new MovimentoEstoqueRequestDTO();
        dto.setProdutoId(produto.getId());
        dto.setDepositoId(deposito.getId());
        dto.setQuantidade(15);

        Estoque estoque = Estoque.builder().produto(produto).deposito(deposito).quantidade(10).build();

        when(produtoRepository.findById(produto.getId())).thenReturn(Optional.of(produto));
        when(depositoRepository.findById(deposito.getId())).thenReturn(Optional.of(deposito));
        when(usuarioRepository.findById(usuario.getId())).thenReturn(Optional.of(usuario));
        when(estoqueRepository.findByProdutoAndDeposito(produto, deposito)).thenReturn(Optional.of(estoque));

        Exception exception = assertThrows(RuntimeException.class, () -> movimentoService.registrarSaida(dto, usuario.getId()));

        assertEquals("Saldo insuficiente para saída.", exception.getMessage());
    }

    @Test
    void gerarRelatorio_deveChamarRepository() {
        MovimentoEstoque mov1 = MovimentoEstoque.builder().id(1L).tipo(TipoMovimentoEnum.ENTRADA).build();
        MovimentoEstoque mov2 = MovimentoEstoque.builder().id(2L).tipo(TipoMovimentoEnum.SAIDA).build();

        when(movimentoEstoqueRepository.findAll(any(org.springframework.data.jpa.domain.Specification.class))).thenReturn(List.of(mov1, mov2));

        MovimentoEstoqueRelatorioRequestDTO filtro = new MovimentoEstoqueRelatorioRequestDTO();
        List<MovimentoEstoque> resultado = movimentoService.gerarRelatorio(filtro);

        assertEquals(2, resultado.size());
        verify(movimentoEstoqueRepository, times(1)).findAll(any(org.springframework.data.jpa.domain.Specification.class));
    }
}
