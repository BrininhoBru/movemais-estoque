package com.movemais_estoque.movemais_estoque.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EstoqueDTO {
    private Long produtoId;
    private Long depositoId;
    private Integer quantidade;
}
