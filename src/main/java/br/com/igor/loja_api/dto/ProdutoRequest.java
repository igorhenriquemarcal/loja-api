package br.com.igor.loja_api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;

// a "ficha" que chega no POST /produtos
// extra = garantia (Eletrônico), tamanho (Roupa) ou validade (Alimento)
public record ProdutoRequest(

        @NotBlank(message = "O tipo é obrigatório.")
        String tipo,

        @Positive(message = "O código deve ser maior que zero.")
        int codigo,

        @NotBlank(message = "O nome é obrigatório.")
        @Size(max = 100, message = "O nome pode ter no máximo 100 caracteres.")
        String nome,

        @Positive(message = "O preço deve ser maior que zero.")
        double preco,

        @PositiveOrZero(message = "O estoque não pode ser negativo.")
        int estoque,

        @NotBlank(message = "O extra é obrigatório.")
        String extra
) {
}