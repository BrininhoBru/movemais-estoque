package com.movemais_estoque.movemais_estoque.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProdutoDTO {
    private String sku;
    private String nome;
    private boolean ativo;
}