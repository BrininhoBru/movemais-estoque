package com.movemais_estoque.movemais_estoque.dto.produto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProdutoUpdateDTO {
    private String sku;
    private String nome;
    private boolean ativo;
}
