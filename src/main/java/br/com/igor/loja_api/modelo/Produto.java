package br.com.igor.loja_api.modelo;

import java.math.BigDecimal;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorColumn;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "tipo")
public abstract class Produto {

    @Id
    private int codigo;
    private String nome;
    // dinheiro é BigDecimal, nunca double: double erra centavos (0.1 + 0.2 dá 0.30000000000000004)
    // no banco: até 10 dígitos, sendo 2 depois da vírgula (99999999,99)
    @Column(precision = 10, scale = 2)
    private BigDecimal preco;
    private int estoque;

    // o JPA precisa de um construtor vazio para recriar o objeto a partir do banco
    protected Produto() {
    }

    public Produto(int codigo, String nome, BigDecimal preco, int estoque) {
        this.codigo = codigo;
        this.nome = nome;
        this.preco = preco;
        this.estoque = estoque;
    }

    public int getCodigo() {
        return codigo;
    }

    public void setCodigo(int codigo) {
        this.codigo = codigo;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public BigDecimal getPreco() {
        return preco;
    }

    public void setPreco(BigDecimal preco) {
        this.preco = preco;
    }

    public int getEstoque() {
        return estoque;
    }

    public void setEstoque(int estoque) {
        this.estoque = estoque;
    }

    public abstract String descricaoExtra();

    public abstract BigDecimal calcularFrete();

    public void baixarEstoque(int quantidade) throws EstoqueInsuficienteException {
        if (quantidade <= 0) {
            throw new EstoqueInsuficienteException("Quantidade deve ser maior que zero.");
        }
        if (quantidade > estoque) {
            throw new EstoqueInsuficienteException("Estoque insuficiente de " + nome + ". Disponível: " + estoque);
        }
        estoque -= quantidade;
    }
}