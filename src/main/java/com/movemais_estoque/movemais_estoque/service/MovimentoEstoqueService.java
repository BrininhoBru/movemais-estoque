package com.movemais_estoque.movemais_estoque.service;

import com.movemais_estoque.movemais_estoque.dto.MovimentoEstoqueDTO;
import com.movemais_estoque.movemais_estoque.enums.TipoMovimentoEnum;
import com.movemais_estoque.movemais_estoque.model.*;
import com.movemais_estoque.movemais_estoque.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MovimentoEstoqueService {

    private final ProdutoRepository produtoRepository;
    private final DepositoRepository depositoRepository;
    private final UsuarioRepository usuarioRepository;
    private final EstoqueRepository estoqueRepository;
    private final MovimentoEstoqueRepository movimentoEstoqueRepository;

    @Transactional
    public MovimentoEstoque registrarMovimento(MovimentoEstoqueDTO dto) {

        Produto produto = produtoRepository.findById(dto.getProdutoId()).orElseThrow(() -> new RuntimeException("Produto não encontrado"));
        Deposito deposito = depositoRepository.findById(dto.getDepositoId()).orElseThrow(() -> new RuntimeException("Depósito não encontrado"));
        Usuario usuario = usuarioRepository.findById(dto.getUsuarioId()).orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
        Estoque estoque = estoqueRepository.findByProduto_IdAndDeposito_Id(produto.getId(), deposito.getId()).orElseGet(() -> Estoque.builder().produto(produto).deposito(deposito).quantidade(0).build());
        TipoMovimentoEnum tipo = TipoMovimentoEnum.valueOf(dto.getTipo().toUpperCase());

        if (tipo == TipoMovimentoEnum.SAIDA) {
            if (estoque.getQuantidade() < dto.getQuantidade()) {
                throw new RuntimeException("Saída excede o estoque atual. Estoque disponível: " + estoque.getQuantidade());
            }
            estoque.setQuantidade(estoque.getQuantidade() - dto.getQuantidade());
        } else if (tipo == TipoMovimentoEnum.ENTRADA) {
            estoque.setQuantidade(estoque.getQuantidade() + dto.getQuantidade());
        }

        estoqueRepository.save(estoque);

        MovimentoEstoque movimento = MovimentoEstoque.builder().produto(produto).deposito(deposito).usuarioResponsavel(usuario).quantidade(dto.getQuantidade()).tipo(tipo).observacao(dto.getObservacao()).build(); // createdAt automático por @CreationTimestamp

        return movimentoEstoqueRepository.save(movimento);
    }
}
