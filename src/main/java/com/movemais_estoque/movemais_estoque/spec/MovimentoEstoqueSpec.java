package com.movemais_estoque.movemais_estoque.spec;

import com.movemais_estoque.movemais_estoque.model.MovimentoEstoque;
import com.movemais_estoque.movemais_estoque.enums.TipoMovimentoEnum;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDateTime;
import java.util.List;

public class MovimentoEstoqueSpec {

    public static Specification<MovimentoEstoque> filtrar(
            LocalDateTime dataInicio,
            LocalDateTime dataFim,
            List<TipoMovimentoEnum> tipos,
            List<Long> produtos,
            List<Long> depositos
    ) {
        return (root, query, cb) -> {
            var predicates = cb.conjunction();

            if (dataInicio != null && dataFim != null) {
                predicates = cb.and(predicates, cb.between(root.get("dataHoraMovimento"), dataInicio, dataFim));
            }

            if (tipos != null && !tipos.isEmpty()) {
                predicates = cb.and(predicates, root.get("tipo").in(tipos));
            }

            if (produtos != null && !produtos.isEmpty()) {
                predicates = cb.and(predicates, root.get("produto").get("id").in(produtos));
            }

            if (depositos != null && !depositos.isEmpty()) {
                predicates = cb.and(predicates, root.get("deposito").get("id").in(depositos));
            }

            return predicates;
        };
    }
}