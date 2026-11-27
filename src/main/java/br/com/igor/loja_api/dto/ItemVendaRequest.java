package br.com.igor.loja_api.dto;

import jakarta.validation.constraints.Positive;

// a "ficha" que chega no POST /vendas/{id}/itens
// o produto pode vir pelo código de barras (o "bip") OU pelo código interno (digitado)
public record ItemVendaRequest(

        String codigoBarras,

        Integer codigoProduto,   // Integer (e não int) para poder vir vazio (null)

        @Positive(message = "A quantidade deve ser maior que zero.")
        int quantidade
) {
}
