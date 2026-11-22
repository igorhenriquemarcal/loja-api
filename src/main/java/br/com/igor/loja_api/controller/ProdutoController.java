package br.com.igor.loja_api.controller;

import java.util.List;
import java.util.Map;

import org.springframework.web.bind.annotation.PutMapping;
import br.com.igor.loja_api.dto.ProdutoUpdateRequest;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.com.igor.loja_api.dto.ProdutoRequest;
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

    // POST /produtos (o produto novo vem no corpo do pedido, em JSON)
    @PostMapping
    public ResponseEntity<?> cadastrar(@RequestBody ProdutoRequest dados) {
        try {
            Produto produto = produtoService.cadastrar(dados);
            return ResponseEntity.status(HttpStatus.CREATED).body(produto);   // 201 + o produto criado
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("erro", e.getMessage()));   // 400 + o motivo
        }
    }

    // DELETE /produtos/1
    @DeleteMapping("/{codigo}")
    public ResponseEntity<Void> remover(@PathVariable int codigo) {
        if (!produtoService.remover(codigo)) {
            return ResponseEntity.notFound().build();    // 404: não existia
        }
        return ResponseEntity.noContent().build();       // 204: apagado
    }

    // PUT /produtos/1  (os dados novos vêm no corpo do pedido, em JSON)
    @PutMapping("/{codigo}")
    public ResponseEntity<?> atualizar(@PathVariable int codigo,
                                       @RequestBody ProdutoUpdateRequest dados) {
        try {
            Produto produto = produtoService.atualizar(codigo, dados);
            if (produto == null) {
                return ResponseEntity.notFound().build();                       // 404: não existe
            }
            return ResponseEntity.ok(produto);                                  // 200: alterado
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("erro", e.getMessage()));   // 400: dados inválidos
        }
    }
}