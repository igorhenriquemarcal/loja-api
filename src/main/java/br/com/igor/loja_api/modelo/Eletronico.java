package br.com.igor.loja_api.modelo;

import java.math.BigDecimal;
import java.math.RoundingMode;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("ELETRONICO")
public class Eletronico extends Produto {

    private int garantiaMeses;

    // construtor vazio exigido pelo JPA
    protected Eletronico() {
    }

    public Eletronico(int codigo, String nome, BigDecimal preco, int estoque, int garantiaMeses) {
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
    public BigDecimal calcularFrete() {
        // 5% do preço, arredondado para centavos
        return getPreco().multiply(new BigDecimal("0.05")).setScale(2, RoundingMode.HALF_EVEN);
    }
}