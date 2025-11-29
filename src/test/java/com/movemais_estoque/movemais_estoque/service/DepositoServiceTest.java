package com.movemais_estoque.movemais_estoque.service;

import com.movemais_estoque.movemais_estoque.dto.deposito.DepositoUpdateDTO;
import com.movemais_estoque.movemais_estoque.model.Deposito;
import com.movemais_estoque.movemais_estoque.repository.DepositoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class DepositoServiceTest {

    @InjectMocks
    private DepositoService depositoService;

    @Mock
    private DepositoRepository depositoRepository;

    private Deposito depositoExemplo;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        depositoExemplo = Deposito.builder()
                .id(1L)
                .codigo("DEP001")
                .nome("Depósito Teste")
                .endereco("Rua A")
                .ativo(true)
                .build();
    }

    @Test
    void criarDeposito_comCodigoNaoCadastrado_deveSalvar() {
        when(depositoRepository.existsByCodigo(depositoExemplo.getCodigo())).thenReturn(false);
        when(depositoRepository.save(depositoExemplo)).thenReturn(depositoExemplo);

        Deposito resultado = depositoService.criar(depositoExemplo);

        assertEquals(depositoExemplo.getCodigo(), resultado.getCodigo());
        verify(depositoRepository, times(1)).save(depositoExemplo);
    }

    @Test
    void criarDeposito_comCodigoJaCadastrado_deveLancarExcecao() {
        when(depositoRepository.existsByCodigo(depositoExemplo.getCodigo())).thenReturn(true);

        Exception exception = assertThrows(IllegalArgumentException.class, () -> depositoService.criar(depositoExemplo));
        assertEquals("Código do depósito já cadastrado.", exception.getMessage());
        verify(depositoRepository, never()).save(any());
    }

    @Test
    void buscarPorId_comIdExistente_deveRetornarDeposito() {
        when(depositoRepository.findById(1L)).thenReturn(Optional.of(depositoExemplo));

        Deposito resultado = depositoService.buscarPorId(1L);

        assertNotNull(resultado);
        assertEquals(depositoExemplo.getId(), resultado.getId());
    }

    @Test
    void buscarPorId_comIdInexistente_deveLancarExcecao() {
        when(depositoRepository.findById(2L)).thenReturn(Optional.empty());

        Exception exception = assertThrows(IllegalArgumentException.class, () -> depositoService.buscarPorId(2L));
        assertEquals("Depósito não encontrado.", exception.getMessage());
    }

    @Test
    void atualizarDeposito_comCodigoDisponivel_deveAtualizar() {
        DepositoUpdateDTO dto = new DepositoUpdateDTO();
        dto.setCodigo("DEP002");
        dto.setNome("Depósito Atualizado");
        dto.setEndereco("Rua B");

        when(depositoRepository.findById(1L)).thenReturn(Optional.of(depositoExemplo));
        when(depositoRepository.existsByCodigo(dto.getCodigo())).thenReturn(false);
        when(depositoRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        Deposito resultado = depositoService.atualizar(1L, dto);

        assertEquals("DEP002", resultado.getCodigo());
        assertEquals("Depósito Atualizado", resultado.getNome());
        assertEquals("Rua B", resultado.getEndereco());
    }

    @Test
    void atualizarDeposito_comCodigoJaEmUso_deveLancarExcecao() {
        DepositoUpdateDTO dto = new DepositoUpdateDTO();
        dto.setCodigo("DEP_EXISTENTE");
        dto.setNome("Depósito Atualizado");
        dto.setEndereco("Rua B");

        when(depositoRepository.findById(1L)).thenReturn(Optional.of(depositoExemplo));
        when(depositoRepository.existsByCodigo(dto.getCodigo())).thenReturn(true);

        Exception exception = assertThrows(IllegalArgumentException.class, () -> depositoService.atualizar(1L, dto));
        assertEquals("Código do deposito já está em uso.", exception.getMessage());
    }

    @Test
    void inativarDeposito_comIdExistente_deveSetarAtivoFalse() {
        when(depositoRepository.findById(1L)).thenReturn(Optional.of(depositoExemplo));
        when(depositoRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        Deposito resultado = depositoService.inativar(1L);

        assertFalse(resultado.isAtivo());
    }

    @Test
    void inativarDeposito_comIdInexistente_deveLancarExcecao() {
        when(depositoRepository.findById(2L)).thenReturn(Optional.empty());

        Exception exception = assertThrows(RuntimeException.class, () -> depositoService.inativar(2L));
        assertEquals("Deposito não encontrado", exception.getMessage());
    }

    @Test
    void listarComFiltros_deveChamarRepository() {
        Page<Deposito> page = new PageImpl<>(List.of(depositoExemplo));
        when(depositoRepository.findAll(any(Specification.class), any(Pageable.class))).thenReturn(page);

        Page<Deposito> resultado = depositoService.listarComFiltros("Teste", null, true, PageRequest.of(0, 10));

        assertEquals(1, resultado.getContent().size());
        verify(depositoRepository, times(1)).findAll(any(Specification.class), any(Pageable.class));
    }
}