package com.movemais_estoque.movemais_estoque.service;

import com.movemais_estoque.movemais_estoque.dto.movimento_estoque.MovimentoEstoqueRelatorioRequestDTO;
import com.movemais_estoque.movemais_estoque.dto.movimento_estoque.MovimentoEstoqueRequestDTO;
import com.movemais_estoque.movemais_estoque.enums.TipoMovimentoEnum;
import com.movemais_estoque.movemais_estoque.model.*;
import com.movemais_estoque.movemais_estoque.repository.*;
import jakarta.persistence.criteria.Predicate;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MovimentoEstoqueService {

    private final MovimentoEstoqueRepository movimentoEstoqueRepository;
    private final EstoqueRepository estoqueRepository;
    private final ProdutoRepository produtoRepository;
    private final DepositoRepository depositoRepository;
    private final UsuarioRepository usuarioRepository;

    @Transactional
    public MovimentoEstoque registrarEntrada(MovimentoEstoqueRequestDTO dto, Long usuarioId) {

        Produto produto = produtoRepository.findById(dto.getProdutoId())
                .orElseThrow(() -> new RuntimeException("Produto não encontrado"));

        Deposito deposito = depositoRepository.findById(dto.getDepositoId())
                .orElseThrow(() -> new RuntimeException("Depósito não encontrado"));

        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        Estoque estoque = estoqueRepository
                .findByProdutoAndDeposito(produto, deposito)
                .orElse(Estoque.builder()
                        .produto(produto)
                        .deposito(deposito)
                        .quantidade(0)
                        .build()
                );

        estoque.setQuantidade(estoque.getQuantidade() + dto.getQuantidade());
        estoqueRepository.save(estoque);

        MovimentoEstoque movimento = MovimentoEstoque.builder()
                .tipo(TipoMovimentoEnum.ENTRADA)
                .produto(produto)
                .deposito(deposito)
                .quantidade(dto.getQuantidade())
                .observacao(dto.getObservacao())
                .usuarioResponsavel(usuario)
                .build();

        return movimentoEstoqueRepository.save(movimento);
    }


    @Transactional
    public MovimentoEstoque registrarSaida(MovimentoEstoqueRequestDTO dto, Long usuarioId) {

        Produto produto = produtoRepository.findById(dto.getProdutoId())
                .orElseThrow(() -> new RuntimeException("Produto não encontrado"));

        Deposito deposito = depositoRepository.findById(dto.getDepositoId())
                .orElseThrow(() -> new RuntimeException("Depósito não encontrado"));

        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        Estoque estoque = estoqueRepository
                .findByProdutoAndDeposito(produto, deposito)
                .orElseThrow(() -> new RuntimeException("Estoque não cadastrado"));

        if (estoque.getQuantidade() < dto.getQuantidade()) {
            throw new RuntimeException("Saldo insuficiente para saída.");
        }

        estoque.setQuantidade(estoque.getQuantidade() - dto.getQuantidade());
        estoqueRepository.save(estoque);

        MovimentoEstoque movimento = MovimentoEstoque.builder()
                .tipo(TipoMovimentoEnum.SAIDA)
                .produto(produto)
                .deposito(deposito)
                .quantidade(dto.getQuantidade())
                .observacao(dto.getObservacao())
                .usuarioResponsavel(usuario)
                .build();

        return movimentoEstoqueRepository.save(movimento);
    }

    public List<MovimentoEstoque> gerarRelatorio(MovimentoEstoqueRelatorioRequestDTO filtro) {
        return movimentoEstoqueRepository.findAll((root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (filtro.getDataInicio() != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("dataHoraMovimento"), filtro.getDataInicio()));
            }
            if (filtro.getDataFim() != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("dataHoraMovimento"), filtro.getDataFim()));
            }
            if (filtro.getTipos() != null && !filtro.getTipos().isEmpty()) {
                predicates.add(root.get("tipo").in(filtro.getTipos()));
            }
            if (filtro.getProdutos() != null && !filtro.getProdutos().isEmpty()) {
                predicates.add(root.get("produto").get("id").in(filtro.getProdutos()));
            }
            if (filtro.getDepositos() != null && !filtro.getDepositos().isEmpty()) {
                predicates.add(root.get("deposito").get("id").in(filtro.getDepositos()));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        });
    }
}
