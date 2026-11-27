package br.com.igor.loja_api.modelo;

import java.math.BigDecimal;
import java.math.RoundingMode;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("ALIMENTO")
public class Alimento extends Produto{

    private String dataValidade;

    // construtor vazio exigido pelo JPA
    protected Alimento() {
    }

    public Alimento(int codigo, String nome, BigDecimal preco, int estoque, String dataValidade){
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
    public BigDecimal calcularFrete(){
        // 10% do preço, arredondado para centavos
        return getPreco().multiply(new BigDecimal("0.10")).setScale(2, RoundingMode.HALF_EVEN);
    }
}
