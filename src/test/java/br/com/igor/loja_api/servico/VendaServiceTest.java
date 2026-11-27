package br.com.igor.loja_api.servico;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import br.com.igor.loja_api.dto.VendaResponse;
import br.com.igor.loja_api.excecao.RecursoNaoEncontradoException;
import br.com.igor.loja_api.modelo.Alimento;
import br.com.igor.loja_api.modelo.EstoqueInsuficienteException;
import br.com.igor.loja_api.modelo.Produto;
import br.com.igor.loja_api.modelo.Roupa;
import br.com.igor.loja_api.modelo.StatusVenda;
import br.com.igor.loja_api.modelo.Venda;
import br.com.igor.loja_api.repositorio.ProdutoRepository;
import br.com.igor.loja_api.repositorio.VendaRepository;

// teste de UNIDADE: testa só a "cozinha" (VendaService), sem Spring e sem banco.
// MockitoExtension liga o Mockito neste teste
@ExtendWith(MockitoExtension.class)
class VendaServiceTest {

    // @Mock = uma "despensa de mentira": não tem banco nenhum atrás,
    // só responde o que a gente combinar com when(...).thenReturn(...)
    @Mock
    private VendaRepository vendaRepository;

    @Mock
    private ProdutoRepository produtoRepository;

    // @InjectMocks = cria o VendaService DE VERDADE, entregando os mocks acima no construtor
    @InjectMocks
    private VendaService vendaService;

    private Produto arroz;
    private Produto camiseta;
    private Venda venda;

    // roda antes de CADA teste: todo teste começa com produtos e venda "novinhos"
    @BeforeEach
    void prepararCenario() {
        arroz = new Alimento(3, "Arroz 5kg", new BigDecimal("30.00"), 50, "31/12/2026");
        camiseta = new Roupa(2, "Camiseta", new BigDecimal("80.00"), 20, "M");

        venda = new Venda();
        venda.adicionarItem(arroz, 2);
        venda.adicionarItem(camiseta, 1);
    }

    @Test
    @DisplayName("finalizar: baixa o estoque de cada item e marca a venda como FINALIZADA")
    void finalizarBaixaEstoque() {
        // ARRANGE (preparar): quando pedirem a venda 1, a despensa de mentira entrega a nossa venda
        when(vendaRepository.findById(1L)).thenReturn(Optional.of(venda));

        // ACT (agir)
        VendaResponse resposta = vendaService.finalizar(1L);

        // ASSERT (conferir)
        assertEquals(StatusVenda.FINALIZADA, resposta.status());
        assertNotNull(resposta.dataHoraFinalizacao());
        assertEquals(48, arroz.getEstoque());      // 50 - 2
        assertEquals(19, camiseta.getEstoque());   // 20 - 1
        assertEquals(new BigDecimal("140.00"), resposta.total());   // 2 x 30 + 1 x 80

        // verify = confere se o service CHAMOU a despensa para gravar a venda
        verify(vendaRepository).save(venda);
    }

    @Test
    @DisplayName("finalizar: se UM item não tem estoque, NENHUM estoque é baixado")
    void finalizarSemEstoqueNaoMexeEmNada() {
        when(vendaRepository.findById(1L)).thenReturn(Optional.of(venda));

        // alguém vendeu camisetas em outro caixa enquanto esta venda estava aberta
        camiseta.setEstoque(0);

        // assertThrows: o teste PASSA se essa exceção for lançada
        EstoqueInsuficienteException erro = assertThrows(EstoqueInsuficienteException.class,
                () -> vendaService.finalizar(1L));

        assertEquals("Estoque insuficiente de Camiseta. Disponível: 0", erro.getMessage());
        assertEquals(50, arroz.getEstoque());              // o arroz NÃO foi baixado
        assertEquals(StatusVenda.ABERTA, venda.getStatus()); // a venda continua aberta
        verify(vendaRepository, never()).save(any());       // e nada foi gravado
    }

    @Test
    @DisplayName("finalizar: venda sem itens é recusada")
    void finalizarVendaVazia() {
        Venda vazia = new Venda();
        when(vendaRepository.findById(2L)).thenReturn(Optional.of(vazia));

        assertThrows(IllegalArgumentException.class, () -> vendaService.finalizar(2L));
        assertEquals(StatusVenda.ABERTA, vazia.getStatus());
    }

    @Test
    @DisplayName("finalizar: não dá para finalizar duas vezes (o estoque não pode baixar em dobro)")
    void finalizarDuasVezes() {
        when(vendaRepository.findById(1L)).thenReturn(Optional.of(venda));

        vendaService.finalizar(1L);
        assertThrows(IllegalArgumentException.class, () -> vendaService.finalizar(1L));

        assertEquals(48, arroz.getEstoque());   // baixou só UMA vez
    }

    @Test
    @DisplayName("finalizar: venda que não existe dá RecursoNaoEncontradoException (vira 404)")
    void finalizarVendaInexistente() {
        // a despensa de mentira responde "não achei"
        when(vendaRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(RecursoNaoEncontradoException.class, () -> vendaService.finalizar(99L));
    }
}
