package com.movemais_estoque.movemais_estoque.dto;

import lombok.Data;

@Data
public class ProdutoDTO {
    private String sku;
    private String nome;
    private boolean ativo;
}