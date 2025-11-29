package com.movemais_estoque.movemais_estoque.dto.movimento_estoque;

import com.movemais_estoque.movemais_estoque.enums.TipoMovimentoEnum;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class MovimentoEstoqueRelatorioResponseDTO {
    private Long id;
    private String produtoNome;
    private String depositoNome;
    private TipoMovimentoEnum tipo;
    private Integer quantidade;
    private LocalDateTime dataHoraMovimento;
    private String observacao;
    private String usuarioResponsavel;
}
