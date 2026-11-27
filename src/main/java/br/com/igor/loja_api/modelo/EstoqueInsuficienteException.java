package br.com.igor.loja_api.modelo;

// antes era "extends Exception" (checked). Virou RuntimeException (unchecked) por dois motivos:
// 1. não obriga a espalhar "throws" e try/catch pelo código;
// 2. o @Transactional do Spring só desfaz a transação (rollback) sozinho com RuntimeException.
//    Com uma Exception checked, a venda poderia ficar pela metade no banco!
public class EstoqueInsuficienteException extends RuntimeException {

    public EstoqueInsuficienteException(String mensagem) {
        super(mensagem);
    }
}
