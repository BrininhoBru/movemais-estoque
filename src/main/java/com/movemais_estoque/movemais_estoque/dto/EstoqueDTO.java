package com.movemais_estoque.movemais_estoque.dto;

import lombok.Data;

@Data
public class EstoqueDTO {
    private Long produtoId;
    private Long depositoId;
    private Integer quantidade;
}
