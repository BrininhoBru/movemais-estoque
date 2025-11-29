package com.movemais_estoque.movemais_estoque.dto.produto;

import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProdutoUpdateDTO {

    @Size(max = 255, message = "SKU deve ter no máximo 255 caracteres")
    private String sku;

    @Size(max = 255, message = "Nome deve ter no máximo 255 caracteres")
    private String nome;

    private Boolean ativo;
}
