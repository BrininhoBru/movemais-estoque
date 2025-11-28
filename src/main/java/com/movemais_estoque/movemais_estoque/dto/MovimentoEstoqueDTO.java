package com.movemais_estoque.movemais_estoque.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class MovimentoEstoqueDTO {
    private Long produtoId;
    private Long depositoId;
    private Integer quantidade;
    private String tipo;
    private LocalDateTime dataHoraMovimento;
    private String observacao;
    private Long usuarioId;
}
