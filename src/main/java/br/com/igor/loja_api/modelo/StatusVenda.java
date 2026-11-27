package br.com.igor.loja_api.modelo;

// em que "momento da vida" a venda está
public enum StatusVenda {
    ABERTA,      // o caixa ainda está passando os produtos
    FINALIZADA,  // paga e com estoque baixado
    CANCELADA    // desistiu: o estoque volta (se já tinha baixado)
}
