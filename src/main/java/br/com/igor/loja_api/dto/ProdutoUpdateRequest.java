package br.com.igor.loja_api.dto;

import java.math.BigDecimal;

import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;

// a "ficha" que chega no PUT /produtos/{codigo}
// só o que pode mudar: tipo e código ficam como estão
public record ProdutoUpdateRequest(

        @NotBlank(message = "O nome é obrigatório.")
        @Size(max = 100, message = "O nome pode ter no máximo 100 caracteres.")
        String nome,

        @NotNull(message = "O preço é obrigatório.")
        @Positive(message = "O preço deve ser maior que zero.")
        @Digits(integer = 8, fraction = 2, message = "O preço deve ter no máximo 8 dígitos inteiros e 2 casas decimais.")
        BigDecimal preco,

        @PositiveOrZero(message = "O estoque não pode ser negativo.")
        int estoque,

        // PUT troca tudo: se não vier, o produto fica sem código de barras
        @Pattern(regexp = "\\d{8}|\\d{13}", message = "O código de barras deve ter 8 ou 13 números.")
        String codigoBarras
) {
}