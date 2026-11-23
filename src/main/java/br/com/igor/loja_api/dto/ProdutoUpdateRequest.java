package br.com.igor.loja_api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;

// a "ficha" que chega no PUT /produtos/{codigo}
// só o que pode mudar: tipo e código ficam como estão
public record ProdutoUpdateRequest(

        @NotBlank(message = "O nome é obrigatório.")
        @Size(max = 100, message = "O nome pode ter no máximo 100 caracteres.")
        String nome,

        @Positive(message = "O preço deve ser maior que zero.")
        double preco,

        @PositiveOrZero(message = "O estoque não pode ser negativo.")
        int estoque
) {
}