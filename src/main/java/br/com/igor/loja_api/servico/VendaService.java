package br.com.igor.loja_api.servico;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.igor.loja_api.dto.ItemVendaRequest;
import br.com.igor.loja_api.dto.VendaResponse;
import br.com.igor.loja_api.excecao.RecursoNaoEncontradoException;
import br.com.igor.loja_api.modelo.Produto;
import br.com.igor.loja_api.modelo.Venda;
import br.com.igor.loja_api.repositorio.ProdutoRepository;
import br.com.igor.loja_api.repositorio.VendaRepository;

@Service
public class VendaService {

    private final VendaRepository vendaRepository;
    private final ProdutoRepository produtoRepository;

    // a cozinha da venda precisa de duas despensas: a das comandas e a dos produtos
    public VendaService(VendaRepository vendaRepository, ProdutoRepository produtoRepository) {
        this.vendaRepository = vendaRepository;
        this.produtoRepository = produtoRepository;
    }

    // @Transactional: tudo dentro do método acontece numa "transação" do banco
    // (a gente aprofunda isso no passo 1.3, quando a venda mexer no estoque)
    @Transactional
    public VendaResponse abrir() {
        Venda venda = vendaRepository.save(new Venda());
        return VendaResponse.de(venda);
    }

    @Transactional(readOnly = true)
    public VendaResponse buscar(Long id) {
        return VendaResponse.de(buscarVenda(id));
    }

    @Transactional
    public VendaResponse adicionarItem(Long vendaId, ItemVendaRequest dados) {
        Venda venda = buscarVenda(vendaId);
        Produto produto = buscarProduto(dados);

        venda.adicionarItem(produto, dados.quantidade());   // a regra mora na própria Venda

        vendaRepository.flush();   // grava agora, para o item novo já ganhar o id na resposta
        return VendaResponse.de(venda);
    }

    @Transactional
    public VendaResponse removerItem(Long vendaId, Long itemId) {
        Venda venda = buscarVenda(vendaId);
        venda.removerItem(itemId);
        vendaRepository.flush();
        return VendaResponse.de(venda);
    }

    // fecha a venda e baixa o estoque de todos os itens.
    // @Transactional = "tudo ou nada": se QUALQUER coisa der erro no meio (RuntimeException),
    // o Spring desfaz tudo (rollback) e o banco fica como estava antes
    @Transactional
    public VendaResponse finalizar(Long vendaId) {
        Venda venda = buscarVenda(vendaId);
        venda.finalizar();              // muda status e estoque dos produtos (em memória)
        vendaRepository.save(venda);    // deixa explícito: grava a venda (os produtos vão junto no commit)
        return VendaResponse.de(venda);
    }

    private Venda buscarVenda(Long id) {
        return vendaRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Venda " + id + " não encontrada."));
    }

    // o produto chega pelo código de barras (bip) ou pelo código interno (digitado)
    private Produto buscarProduto(ItemVendaRequest dados) {
        if (dados.codigoBarras() != null) {
            return produtoRepository.findByCodigoBarras(dados.codigoBarras())
                    .orElseThrow(() -> new RecursoNaoEncontradoException(
                            "Nenhum produto com o código de barras " + dados.codigoBarras() + "."));
        }
        if (dados.codigoProduto() != null) {
            return produtoRepository.findById(dados.codigoProduto())
                    .orElseThrow(() -> new RecursoNaoEncontradoException(
                            "Produto " + dados.codigoProduto() + " não encontrado."));
        }
        throw new IllegalArgumentException("Informe o codigoBarras ou o codigoProduto.");
    }
}
