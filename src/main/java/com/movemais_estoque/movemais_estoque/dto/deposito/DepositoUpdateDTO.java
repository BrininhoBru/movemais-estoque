package com.movemais_estoque.movemais_estoque.dto.deposito;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DepositoUpdateDTO {
    private String codigo;
    private String nome;
    private String endereco;
    private boolean ativo;
}
