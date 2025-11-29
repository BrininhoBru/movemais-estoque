package com.movemais_estoque.movemais_estoque.spec;

import com.movemais_estoque.movemais_estoque.model.Estoque;
import org.springframework.data.jpa.domain.Specification;

public class EstoqueSpecifications {

    public static Specification<Estoque> hasProduto(Long produtoId) {
        return (root, query, cb) -> produtoId == null
                ? cb.conjunction()
                : cb.equal(root.get("produto").get("id"), produtoId);
    }

    public static Specification<Estoque> hasDeposito(Long depositoId) {
        return (root, query, cb) -> depositoId == null
                ? cb.conjunction()
                : cb.equal(root.get("deposito").get("id"), depositoId);
    }
}
