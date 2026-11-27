package br.com.igor.loja_api.excecao;

import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

// trata os erros de TODOS os controllers num lugar só
@RestControllerAdvice
public class TratadorDeErros {

    // ficha que quebrou alguma regra do @Valid (@NotBlank, @Positive...)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> tratarCamposInvalidos(MethodArgumentNotValidException ex) {
        Map<String, String> campos = new LinkedHashMap<>();
        for (FieldError erro : ex.getBindingResult().getFieldErrors()) {
            campos.put(erro.getField(), erro.getDefaultMessage());
        }

        Map<String, Object> corpo = new LinkedHashMap<>();
        corpo.put("erro", "Dados inválidos.");
        corpo.put("campos", campos);
        return ResponseEntity.badRequest().body(corpo);   // 400
    }

    // procurou algo (venda, produto, item) que não existe
    @ExceptionHandler(RecursoNaoEncontradoException.class)
    public ResponseEntity<Map<String, Object>> tratarNaoEncontrado(RecursoNaoEncontradoException ex) {
        Map<String, Object> corpo = new LinkedHashMap<>();
        corpo.put("erro", ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(corpo);   // 404
    }

    // regra de negócio recusada pelo service (código repetido, tipo inválido...)
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, Object>> tratarRegraDeNegocio(IllegalArgumentException ex) {
        Map<String, Object> corpo = new LinkedHashMap<>();
        corpo.put("erro", ex.getMessage());
        return ResponseEntity.badRequest().body(corpo);   // 400
    }
}