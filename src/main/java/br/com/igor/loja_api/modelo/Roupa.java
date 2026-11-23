package br.com.igor.loja_api.modelo;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("ROUPA")
public class Roupa extends Produto {

    private String tamanho;

    // construtor vazio exigido pelo JPA
    protected Roupa() {
    }


    public Roupa(int codigo, String nome, double preco, int estoque, String tamanho) {
        super(codigo, nome, preco, estoque);
        this.tamanho = tamanho;
    }

    public String getTamanho() {
        return tamanho;
    }

    @Override
    public String descricaoExtra() {
        return "Tamanho: " + tamanho;
    }

    @Override
    public double calcularFrete() {
        return 15.0;
    }

}
