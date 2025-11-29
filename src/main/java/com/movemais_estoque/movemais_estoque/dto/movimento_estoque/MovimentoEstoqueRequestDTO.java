package com.movemais_estoque.movemais_estoque.dto.movimento_estoque;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MovimentoEstoqueRequestDTO {
    private Long produtoId;
    private Long depositoId;
    private Integer quantidade;
    private String observacao;
}
