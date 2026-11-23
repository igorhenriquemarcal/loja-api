package br.com.igor.loja_api.servico;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import br.com.igor.loja_api.dto.ProdutoRequest;
import br.com.igor.loja_api.dto.ProdutoUpdateRequest;
import br.com.igor.loja_api.modelo.Alimento;
import br.com.igor.loja_api.modelo.Eletronico;
import br.com.igor.loja_api.modelo.Produto;
import br.com.igor.loja_api.modelo.Roupa;
import br.com.igor.loja_api.repositorio.ProdutoRepository;

@Service
public class ProdutoService {

    private final ProdutoRepository repositorio;

    // o Spring entrega o repositório pronto aqui
    public ProdutoService(ProdutoRepository repositorio) {
        this.repositorio = repositorio;

        // dados de exemplo: só entram se o banco estiver vazio
        if (repositorio.count() == 0) {
            repositorio.save(new Eletronico(1, "Fone", 200.0, 5, 12));
            repositorio.save(new Roupa(2, "Camiseta", 80.0, 20, "M"));
            repositorio.save(new Alimento(3, "Arroz 5kg", 30.0, 50, "31/12/2026"));
        }
    }

    public List<Produto> listar() {
        return repositorio.findAll(Sort.by("codigo"));
    }

    public List<Produto> buscarPorNome(String nome) {
        return repositorio.findByNomeContainingIgnoreCase(nome);
    }

    // devolve null se não existir
    public Produto buscar(int codigo) {
        return repositorio.findById(codigo).orElse(null);
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

        // 3. o código já existe?
        if (repositorio.existsById(dados.codigo())) {
            throw new IllegalArgumentException("Já existe um produto com o código " + dados.codigo() + ".");
        }

        // 4. monta o produto conforme o tipo
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

        // 5. guarda no banco
        return repositorio.save(produto);
    }

    // devolve o produto alterado, ou null se o código não existir
    public Produto atualizar(int codigo, ProdutoUpdateRequest dados) {
        Produto produto = repositorio.findById(codigo).orElse(null);
        if (produto == null) {
            return null;
        }

        // valida tudo antes de alterar qualquer coisa
        if (dados.nome() == null || dados.nome().isBlank()) {
            throw new IllegalArgumentException("O nome não pode ficar vazio.");
        }
        if (dados.preco() <= 0) {
            throw new IllegalArgumentException("O preço deve ser maior que zero.");
        }
        if (dados.estoque() < 0) {
            throw new IllegalArgumentException("O estoque não pode ser negativo.");
        }

        produto.setNome(dados.nome().trim());
        produto.setPreco(dados.preco());
        produto.setEstoque(dados.estoque());
        return repositorio.save(produto);
    }

    // devolve true se removeu, false se o código não existia
    public boolean remover(int codigo) {
        if (!repositorio.existsById(codigo)) {
            return false;
        }
        repositorio.deleteById(codigo);
        return true;
    }
}