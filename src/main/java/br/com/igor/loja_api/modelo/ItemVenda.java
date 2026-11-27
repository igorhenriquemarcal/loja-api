package br.com.igor.loja_api.modelo;

import java.math.BigDecimal;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

// uma linha do cupom: "2 x Arroz 5kg ... R$ 30,00 ... R$ 60,00"
@Entity
public class ItemVenda {

    // o banco gera o número sozinho (1, 2, 3...)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // muitos itens pertencem a UMA venda -> coluna venda_id na tabela item_venda
    @ManyToOne(optional = false)
    @JoinColumn(name = "venda_id")
    private Venda venda;

    // muitos itens (de vendas diferentes) apontam para UM produto -> coluna produto_codigo
    @ManyToOne(optional = false)
    @JoinColumn(name = "produto_codigo")
    private Produto produto;

    private int quantidade;

    // a "foto" do preço no momento da venda.
    // se o produto mudar de preço amanhã, este valor NÃO muda: o cupom de ontem continua certo
    @Column(precision = 10, scale = 2)
    private BigDecimal precoUnitario;

    // construtor vazio exigido pelo JPA
    protected ItemVenda() {
    }

    public ItemVenda(Venda venda, Produto produto, int quantidade) {
        this.venda = venda;
        this.produto = produto;
        this.quantidade = quantidade;
        this.precoUnitario = produto.getPreco();   // tira a "foto" aqui
    }

    // quantidade x preço da foto (não é guardado no banco: é calculado na hora)
    public BigDecimal getSubtotal() {
        return precoUnitario.multiply(BigDecimal.valueOf(quantidade));
    }

    public Long getId() {
        return id;
    }

    public Venda getVenda() {
        return venda;
    }

    public Produto getProduto() {
        return produto;
    }

    public int getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(int quantidade) {
        this.quantidade = quantidade;
    }

    public BigDecimal getPrecoUnitario() {
        return precoUnitario;
    }
}
