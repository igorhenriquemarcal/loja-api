package br.com.igor.loja_api.modelo;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("ELETRONICO")
public class Eletronico extends Produto {

    private int garantiaMeses;

    // construtor vazio exigido pelo JPA
    protected Eletronico() {
    }

    public Eletronico(int codigo, String nome, double preco, int estoque, int garantiaMeses) {
        super(codigo, nome, preco, estoque);
        this.garantiaMeses = garantiaMeses;
    }

    public int getGarantiaMeses() {
        return garantiaMeses;
    }

    @Override
    public String descricaoExtra() {
        return "Garantia: " + garantiaMeses + " meses";
    }

    @Override
    public double calcularFrete() {
        return getPreco() * 0.05;
    }
}