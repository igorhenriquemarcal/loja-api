package br.com.igor.loja_api.repositorio;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.igor.loja_api.modelo.Produto;

public interface ProdutoRepository extends JpaRepository<Produto, Integer> {

    // o Spring monta a busca sozinho, só pelo nome do método
    List<Produto> findByNomeContainingIgnoreCase(String nome);
}