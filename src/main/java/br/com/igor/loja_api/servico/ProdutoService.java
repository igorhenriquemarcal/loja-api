package br.com.igor.loja_api.servico;

import java.util.List;

import org.springframework.stereotype.Service;

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
}