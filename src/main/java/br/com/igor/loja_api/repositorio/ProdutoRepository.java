package br.com.igor.loja_api.repositorio;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.igor.loja_api.modelo.Produto;

public interface ProdutoRepository extends JpaRepository<Produto, Integer> {

    // o Spring monta a busca sozinho, só pelo nome do método
    List<Produto> findByNomeContainingIgnoreCase(String nome);

    // SELECT ... WHERE codigo_barras = ?   (Optional: pode não achar)
    Optional<Produto> findByCodigoBarras(String codigoBarras);

    // já existe algum produto com esse código de barras?
    boolean existsByCodigoBarras(String codigoBarras);

    // já existe OUTRO produto (código diferente) com esse código de barras?
    boolean existsByCodigoBarrasAndCodigoNot(String codigoBarras, int codigo);
}