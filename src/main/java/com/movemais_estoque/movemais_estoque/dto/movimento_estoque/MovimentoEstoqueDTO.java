package com.movemais_estoque.movemais_estoque.dto.movimento_estoque;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class MovimentoEstoqueDTO {
    private Long produtoId;
    private Long depositoId;
    private Integer quantidade;
    private String tipo;
    private LocalDateTime dataHoraMovimento;
    private String observacao;
    private Long usuarioId;
}
