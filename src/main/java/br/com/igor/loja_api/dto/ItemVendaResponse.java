package br.com.igor.loja_api.dto;

import java.math.BigDecimal;

import br.com.igor.loja_api.modelo.ItemVenda;

// como uma linha do cupom aparece no JSON de resposta
public record ItemVendaResponse(
        Long id,
        int codigoProduto,
        String nomeProduto,
        int quantidade,
        BigDecimal precoUnitario,
        BigDecimal subtotal
) {

    // copia da entidade só o que o cliente precisa ver
    public static ItemVendaResponse de(ItemVenda item) {
        return new ItemVendaResponse(
                item.getId(),
                item.getProduto().getCodigo(),
                item.getProduto().getNome(),
                item.getQuantidade(),
                item.getPrecoUnitario(),
                item.getSubtotal());
    }
}
