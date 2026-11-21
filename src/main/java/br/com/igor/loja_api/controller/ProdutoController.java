package br.com.igor.loja_api.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.com.igor.loja_api.modelo.Produto;
import br.com.igor.loja_api.servico.ProdutoService;

@RestController
@RequestMapping("/produtos")
public class ProdutoController {

    private final ProdutoService produtoService;

    // o Spring entrega o ProdutoService pronto aqui
    public ProdutoController(ProdutoService produtoService) {
        this.produtoService = produtoService;
    }

    // GET /produtos  ou  GET /produtos?nome=fo
    @GetMapping
    public List<Produto> listar(@RequestParam(required = false) String nome) {
        if (nome == null) {
            return produtoService.listar();
        }
        return produtoService.buscarPorNome(nome);
    }

    // GET /produtos/1
    @GetMapping("/{codigo}")
    public ResponseEntity<Produto> buscar(@PathVariable int codigo) {
        Produto produto = produtoService.buscar(codigo);
        if (produto == null) {
            return ResponseEntity.notFound().build();   // 404
        }
        return ResponseEntity.ok(produto);              // 200 + o produto em JSON
    }
}