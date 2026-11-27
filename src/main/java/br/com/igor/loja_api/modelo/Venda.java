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

import br.com.igor.loja_api.excecao.RecursoNaoEncontradoException;

// a "comanda" do caixa: começa ABERTA e vai recebendo itens
@Entity
public class Venda {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // quando a venda foi aberta
    private LocalDateTime dataHora;

    // quando foi paga (fica vazio enquanto a venda está aberta)
    private LocalDateTime dataHoraFinalizacao;

    // quando foi cancelada (fica vazio se não foi)
    private LocalDateTime dataHoraCancelamento;

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

    // passa um produto no caixa.
    // se o produto já está na venda, só aumenta a quantidade (como "bipar" o mesmo arroz duas vezes)
    public ItemVenda adicionarItem(Produto produto, int quantidade) {
        garantirAberta();

        for (ItemVenda item : itens) {
            if (item.getProduto().getCodigo() == produto.getCodigo()) {
                int novaQuantidade = item.getQuantidade() + quantidade;
                verificarEstoque(produto, novaQuantidade);
                item.setQuantidade(novaQuantidade);
                return item;
            }
        }

        verificarEstoque(produto, quantidade);
        ItemVenda novo = new ItemVenda(this, produto, quantidade);
        itens.add(novo);
        return novo;
    }

    // tira uma linha da comanda (o orphanRemoval apaga do banco)
    public void removerItem(Long itemId) {
        garantirAberta();

        boolean removeu = itens.removeIf(item -> item.getId().equals(itemId));
        if (!removeu) {
            throw new RecursoNaoEncontradoException("Item " + itemId + " não encontrado na venda " + id + ".");
        }
    }

    // o cliente pagou: tira os produtos da prateleira e fecha a comanda
    public void finalizar() {
        garantirAberta();
        if (itens.isEmpty()) {
            throw new IllegalArgumentException("A venda " + id + " não tem itens.");
        }

        // 1ª volta: CONFERE todos os itens antes de mexer em qualquer estoque.
        // (se o 3º item não tiver estoque, o 1º e o 2º nem chegam a ser baixados)
        for (ItemVenda item : itens) {
            verificarEstoque(item.getProduto(), item.getQuantidade());
        }

        // 2ª volta: agora sim, BAIXA o estoque de cada um
        for (ItemVenda item : itens) {
            item.getProduto().baixarEstoque(item.getQuantidade());
        }

        status = StatusVenda.FINALIZADA;
        dataHoraFinalizacao = LocalDateTime.now();
    }

    // desistência ou estorno.
    // ABERTA     -> CANCELADA: o estoque nunca foi baixado, então não há nada para devolver
    // FINALIZADA -> CANCELADA: os produtos voltam para a prateleira
    // CANCELADA  -> recusa (senão o estoque seria devolvido duas vezes)
    public void cancelar() {
        if (status == StatusVenda.CANCELADA) {
            throw new IllegalArgumentException("A venda " + id + " já está CANCELADA.");
        }

        if (status == StatusVenda.FINALIZADA) {
            for (ItemVenda item : itens) {
                item.getProduto().devolverEstoque(item.getQuantidade());
            }
        }

        status = StatusVenda.CANCELADA;
        dataHoraCancelamento = LocalDateTime.now();
    }

    // só dá para mexer numa venda que ainda está aberta
    private void garantirAberta() {
        if (status != StatusVenda.ABERTA) {
            throw new IllegalArgumentException("A venda " + id + " está " + status + " e não pode ser alterada.");
        }
    }

    // aqui só CONFERE se tem estoque; a baixa de verdade acontece ao finalizar a venda
    private void verificarEstoque(Produto produto, int quantidadeDesejada) {
        if (quantidadeDesejada > produto.getEstoque()) {
            throw new EstoqueInsuficienteException("Estoque insuficiente de " + produto.getNome()
                    + ". Disponível: " + produto.getEstoque());
        }
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

    public LocalDateTime getDataHoraFinalizacao() {
        return dataHoraFinalizacao;
    }

    public LocalDateTime getDataHoraCancelamento() {
        return dataHoraCancelamento;
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
