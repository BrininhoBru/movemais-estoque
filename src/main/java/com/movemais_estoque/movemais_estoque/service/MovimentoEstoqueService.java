package com.movemais_estoque.movemais_estoque.service;

import com.movemais_estoque.movemais_estoque.dto.MovimentoEstoqueDTO;
import com.movemais_estoque.movemais_estoque.enums.TipoMovimentoEnum;
import com.movemais_estoque.movemais_estoque.model.MovimentoEstoque;
import com.movemais_estoque.movemais_estoque.repository.DepositoRepository;
import com.movemais_estoque.movemais_estoque.repository.MovimentoEstoqueRepository;
import com.movemais_estoque.movemais_estoque.repository.ProdutoRepository;
import com.movemais_estoque.movemais_estoque.repository.UsuarioRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MovimentoEstoqueService {
    private final MovimentoEstoqueRepository movimentoRepository;
    private final EstoqueService estoqueService;
    private final ProdutoRepository produtoRepository;
    private final DepositoRepository depositoRepository;
    private final UsuarioRepository usuarioRepository;

    @Transactional
    public MovimentoEstoque registrarMovimento(MovimentoEstoqueDTO dto) {

        var produto = produtoRepository.findById(dto.getProdutoId()).orElseThrow(() -> new IllegalArgumentException("Produto não encontrado."));

        var deposito = depositoRepository.findById(dto.getDepositoId()).orElseThrow(() -> new IllegalArgumentException("Depósito não encontrado."));

        var usuario = usuarioRepository.findById(dto.getUsuarioId()).orElseThrow(() -> new IllegalArgumentException("Usuário não encontrado."));

        var estoque = estoqueService.consultarSaldo(produto.getId(), deposito.getId());

        if (dto.getTipo().equalsIgnoreCase("ENTRADA")) {
            estoque.setQuantidade(estoque.getQuantidade() + dto.getQuantidade());
        } else if (dto.getTipo().equalsIgnoreCase("SAIDA")) {

            if (estoque.getQuantidade() < dto.getQuantidade()) {
                throw new IllegalArgumentException("Saldo insuficiente para saída.");
            }

            estoque.setQuantidade(estoque.getQuantidade() - dto.getQuantidade());
        }

        estoqueService.salvar(estoque);

        MovimentoEstoque movimento = MovimentoEstoque.builder().produto(produto).deposito(deposito).quantidade(dto.getQuantidade()).observacao(dto.getObservacao()).tipo(TipoMovimentoEnum.valueOf(dto.getTipo())).usuarioResponsavel(usuario).build();

        return movimentoRepository.save(movimento);
    }
}
