package br.com.igor.loja_api.dto;

// a "ficha" que chega no PUT /produtos/{codigo}
// só o que pode mudar: tipo e código ficam como estão
public record ProdutoUpdateRequest(
        String nome,
        double preco,
        int estoque
) {
}