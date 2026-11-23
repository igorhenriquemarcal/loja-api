package br.com.igor.loja_api.modelo;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("ALIMENTO")
public class Alimento extends Produto{

    private String dataValidade;

    // construtor vazio exigido pelo JPA
    protected Alimento() {
    }

    public Alimento(int codigo, String nome, double preco, int estoque, String dataValidade){
        super(codigo, nome, preco, estoque);
        this.dataValidade = dataValidade;
    }

    public String getDataValidade() {
        return dataValidade;
    }

    @Override
    public String descricaoExtra(){
        return "Validade: " + dataValidade;
    }

    @Override
    public double calcularFrete(){
        return getPreco() * 0.10;
    }
}
