package br.com.igor.loja_api;

import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class OlaController {

    @GetMapping("/ola")
    public String ola() {
        return "Olá, API da loja!";
    }

    @GetMapping("/status")
    public Map<String, Object> status() {
        return Map.of(
                "loja", "Loja Java",
                "online", true,
                "produtosCadastrados", 0
        );
    }
}