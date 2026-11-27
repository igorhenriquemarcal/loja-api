package br.com.igor.loja_api.modelo;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;

// a "comanda" do caixa: começa ABERTA e vai recebendo itens
@Entity
public class Venda {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // quando a venda foi aberta
    private LocalDateTime dataHora;

    // STRING: grava "ABERTA" no banco (e não 0, 1, 2), mais fácil de ler e mais seguro
    @Enumerated(EnumType.STRING)
    private StatusVenda status;

    // UMA venda tem MUITOS itens.
    // mappedBy = "venda": quem guarda a ligação no banco é o campo "venda" do ItemVenda
    // cascade = ALL: salvou/apagou a venda, salva/apaga os itens junto
    // orphanRemoval = true: tirou o item da lista, ele é apagado do banco
    @OneToMany(mappedBy = "venda", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ItemVenda> itens = new ArrayList<>();

    // construtor usado pelo JPA e por nós: toda venda nasce aberta, agora
    public Venda() {
        this.dataHora = LocalDateTime.now();
        this.status = StatusVenda.ABERTA;
    }

    // soma dos subtotais dos itens (calculado na hora, não fica no banco)
    public BigDecimal getTotal() {
        BigDecimal total = BigDecimal.ZERO;
        for (ItemVenda item : itens) {
            total = total.add(item.getSubtotal());
        }
        return total;
    }

    public Long getId() {
        return id;
    }

    public LocalDateTime getDataHora() {
        return dataHora;
    }

    public StatusVenda getStatus() {
        return status;
    }

    public void setStatus(StatusVenda status) {
        this.status = status;
    }

    public List<ItemVenda> getItens() {
        return itens;
    }
}
