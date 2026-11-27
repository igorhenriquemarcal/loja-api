package br.com.igor.loja_api.controller;

import java.util.List;

import jakarta.validation.Valid;
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

    // GET /produtos/barras/7891234567895  (o "bip" do caixa)
    @GetMapping("/barras/{codigoBarras}")
    public ResponseEntity<Produto> buscarPorCodigoBarras(@PathVariable String codigoBarras) {
        Produto produto = produtoService.buscarPorCodigoBarras(codigoBarras);
        if (produto == null) {
            return ResponseEntity.notFound().build();   // 404: nenhum produto com esse código
        }
        return ResponseEntity.ok(produto);              // 200
    }

    // POST /produtos (o produto novo vem no corpo do pedido, em JSON)
    // se o service recusar (código repetido, tipo inválido...), o TratadorDeErros devolve o 400
    @PostMapping
    public ResponseEntity<Produto> cadastrar(@Valid @RequestBody ProdutoRequest dados) {
        Produto produto = produtoService.cadastrar(dados);
        return ResponseEntity.status(HttpStatus.CREATED).body(produto);   // 201 + o produto criado
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
    public ResponseEntity<Produto> atualizar(@PathVariable int codigo,
                                             @Valid @RequestBody ProdutoUpdateRequest dados) {
        Produto produto = produtoService.atualizar(codigo, dados);
        if (produto == null) {
            return ResponseEntity.notFound().build();   // 404: não existe
        }
        return ResponseEntity.ok(produto);              // 200: alterado
    }
}