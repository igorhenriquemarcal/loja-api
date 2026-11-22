package br.com.igor.loja_api.dto;

// a "ficha" que chega no POST /produtos
// extra = garantia (Eletrônico), tamanho (Roupa) ou validade (Alimento)
public record ProdutoRequest(
        String tipo,
        int codigo,
        String nome,
        double preco,
        int estoque,
        String extra
) {
}