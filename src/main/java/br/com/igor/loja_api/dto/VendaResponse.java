package br.com.igor.loja_api.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import br.com.igor.loja_api.modelo.StatusVenda;
import br.com.igor.loja_api.modelo.Venda;

// como a venda aparece no JSON de resposta.
// por que não devolver a entidade Venda direto? Venda tem itens, e cada item aponta de volta
// para a Venda... o JSON ficaria em loop infinito. O DTO corta isso e mostra só o necessário.
public record VendaResponse(
        Long id,
        LocalDateTime dataHora,
        StatusVenda status,
        List<ItemVendaResponse> itens,
        BigDecimal total
) {

    public static VendaResponse de(Venda venda) {
        List<ItemVendaResponse> itens = venda.getItens().stream()
                .map(ItemVendaResponse::de)
                .toList();
        return new VendaResponse(venda.getId(), venda.getDataHora(), venda.getStatus(), itens, venda.getTotal());
    }
}
