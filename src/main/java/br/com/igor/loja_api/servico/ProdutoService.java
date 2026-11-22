package br.com.igor.loja_api.servico;

import java.util.List;

import org.springframework.stereotype.Service;

import br.com.igor.loja_api.dto.ProdutoRequest;
import br.com.igor.loja_api.modelo.Alimento;
import br.com.igor.loja_api.modelo.Catalogo;
import br.com.igor.loja_api.modelo.Eletronico;
import br.com.igor.loja_api.modelo.Produto;
import br.com.igor.loja_api.modelo.Roupa;

@Service
public class ProdutoService {

    private final Catalogo catalogo = new Catalogo();

    // dados temporários, até termos banco de dados
    public ProdutoService() {
        catalogo.adicionar(new Eletronico(1, "Fone", 200.0, 5, 12));
        catalogo.adicionar(new Roupa(2, "Camiseta", 80.0, 20, "M"));
        catalogo.adicionar(new Alimento(3, "Arroz 5kg", 30.0, 50, "31/12/2026"));
    }

    public List<Produto> listar() {
        return catalogo.listar();
    }

    public List<Produto> buscarPorNome(String nome) {
        return catalogo.buscarPorNome(nome);
    }

    // devolve null se não existir
    public Produto buscar(int codigo) {
        return catalogo.buscar(codigo);
    }

    // lê a ficha, confere os dados e cria o produto do tipo certo
    public Produto cadastrar(ProdutoRequest dados) {
        // 1. campos de texto preenchidos?
        if (dados.tipo() == null || dados.nome() == null || dados.nome().isBlank()
                || dados.extra() == null || dados.extra().isBlank()) {
            throw new IllegalArgumentException("Preencha tipo, nome e extra.");
        }

        // 2. números fazem sentido?
        if (dados.preco() <= 0) {
            throw new IllegalArgumentException("O preço deve ser maior que zero.");
        }
        if (dados.estoque() < 0) {
            throw new IllegalArgumentException("O estoque não pode ser negativo.");
        }

        // 3. monta o produto conforme o tipo
        String nome = dados.nome().trim();
        String extra = dados.extra().trim();
        Produto produto;

        switch (dados.tipo().toUpperCase()) {
            case "ELETRONICO":
                int garantia;
                try {
                    garantia = Integer.parseInt(extra);
                } catch (NumberFormatException e) {
                    throw new IllegalArgumentException("Para eletrônico, o extra deve ser a garantia em meses.");
                }
                produto = new Eletronico(dados.codigo(), nome, dados.preco(), dados.estoque(), garantia);
                break;
            case "ROUPA":
                produto = new Roupa(dados.codigo(), nome, dados.preco(), dados.estoque(), extra.toUpperCase());
                break;
            case "ALIMENTO":
                produto = new Alimento(dados.codigo(), nome, dados.preco(), dados.estoque(), extra);
                break;
            default:
                throw new IllegalArgumentException("Tipo inválido. Use ELETRONICO, ROUPA ou ALIMENTO.");
        }

        // 4. o catálogo recusa código repetido
        if (!catalogo.adicionar(produto)) {
            throw new IllegalArgumentException("Já existe um produto com o código " + dados.codigo() + ".");
        }

        return produto;
    }
}