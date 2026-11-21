package br.com.igor.loja_api.modelo;

public class EstoqueInsuficienteException extends Exception{

    public EstoqueInsuficienteException(String mensagem){
        super (mensagem);
    }
}
