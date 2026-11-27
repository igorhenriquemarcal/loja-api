package br.com.igor.loja_api.controller;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.igor.loja_api.dto.ItemVendaRequest;
import br.com.igor.loja_api.dto.VendaResponse;
import br.com.igor.loja_api.servico.VendaService;

// o garçom das vendas: recebe o pedido, passa para a cozinha (VendaService) e devolve a resposta.
// erros (404, 400) são devolvidos pelo TratadorDeErros, por isso não tem try/catch aqui
@RestController
@RequestMapping("/vendas")
public class VendaController {

    private final VendaService vendaService;

    public VendaController(VendaService vendaService) {
        this.vendaService = vendaService;
    }

    // POST /vendas  -> abre uma venda vazia
    @PostMapping
    public ResponseEntity<VendaResponse> abrir() {
        return ResponseEntity.status(HttpStatus.CREATED).body(vendaService.abrir());   // 201
    }

    // GET /vendas/1  -> mostra a venda com itens e total
    @GetMapping("/{id}")
    public VendaResponse buscar(@PathVariable Long id) {
        return vendaService.buscar(id);   // 200
    }

    // POST /vendas/1/itens  -> passa um produto no caixa
    @PostMapping("/{id}/itens")
    public VendaResponse adicionarItem(@PathVariable Long id, @Valid @RequestBody ItemVendaRequest dados) {
        return vendaService.adicionarItem(id, dados);   // 200 + a venda atualizada
    }

    // POST /vendas/1/finalizar  -> cliente pagou: baixa o estoque e fecha a venda
    // (POST porque é uma AÇÃO que muda várias coisas, não só "trocar um campo")
    @PostMapping("/{id}/finalizar")
    public VendaResponse finalizar(@PathVariable Long id) {
        return vendaService.finalizar(id);   // 200 + a venda FINALIZADA
    }

    // DELETE /vendas/1/itens/5  -> tira um item da venda
    @DeleteMapping("/{id}/itens/{itemId}")
    public VendaResponse removerItem(@PathVariable Long id, @PathVariable Long itemId) {
        return vendaService.removerItem(id, itemId);   // 200 + a venda atualizada
    }
}
