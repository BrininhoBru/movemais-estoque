package com.movemais_estoque.movemais_estoque.dto.deposito;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DepositoUpdateDTO {

    @NotBlank(message = "Código é obrigatório")
    @Size(max = 255, message = "Código deve ter no máximo 255 caracteres")
    private String codigo;

    @NotBlank(message = "Nome é obrigatório")
    @Size(max = 100, message = "Nome deve ter no máximo 100 caracteres")
    private String nome;

    @NotBlank(message = "Endereço é obrigatório")
    @Size(max = 100, message = "Endereço deve ter no máximo 100 caracteres")
    private String endereco;

    private boolean ativo;
}
