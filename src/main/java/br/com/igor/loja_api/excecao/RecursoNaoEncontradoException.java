package br.com.igor.loja_api.excecao;

// "procurei e não achei": o TratadorDeErros transforma em 404
// RuntimeException = não obriga ninguém a escrever try/catch ou "throws"
public class RecursoNaoEncontradoException extends RuntimeException {

    public RecursoNaoEncontradoException(String mensagem) {
        super(mensagem);
    }
}
