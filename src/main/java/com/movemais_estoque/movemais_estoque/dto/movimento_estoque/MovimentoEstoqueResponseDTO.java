package com.movemais_estoque.movemais_estoque.dto.movimento_estoque;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class MovimentoEstoqueResponseDTO {
    private Long id;
    private String tipo;
    private String produto;
    private String deposito;
    private Integer quantidade;
    private LocalDateTime dataHoraMovimento;
    private String observacao;
    private String usuarioResponsavel;
}
