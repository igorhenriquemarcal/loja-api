package br.com.igor.loja_api.dto;

import java.math.BigDecimal;

import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
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

        // BigDecimal é objeto: pode chegar null, por isso o @NotNull
        @NotNull(message = "O preço é obrigatório.")
        @Positive(message = "O preço deve ser maior que zero.")
        @Digits(integer = 8, fraction = 2, message = "O preço deve ter no máximo 8 dígitos inteiros e 2 casas decimais.")
        BigDecimal preco,

        @PositiveOrZero(message = "O estoque não pode ser negativo.")
        int estoque,

        @NotBlank(message = "O extra é obrigatório.")
        String extra,

        // opcional: se vier null, passa; se vier, precisa ter 8 ou 13 números
        @Pattern(regexp = "\\d{8}|\\d{13}", message = "O código de barras deve ter 8 ou 13 números.")
        String codigoBarras
) {
}