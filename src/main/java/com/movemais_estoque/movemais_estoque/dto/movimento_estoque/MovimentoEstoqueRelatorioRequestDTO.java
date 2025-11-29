package com.movemais_estoque.movemais_estoque.dto.movimento_estoque;

import com.movemais_estoque.movemais_estoque.enums.TipoMovimentoEnum;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class MovimentoEstoqueRelatorioRequestDTO {
    private LocalDateTime dataInicio;
    private LocalDateTime dataFim;
    private List<TipoMovimentoEnum> tipos;
    private List<Long> produtos;
    private List<Long> depositos;
}