package com.marketplace.service;

import com.marketplace.model.Avaliacao;
import com.marketplace.repository.AvaliacaoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AvaliacaoServiceTest {

    private AvaliacaoRepository repository;
    private AvaliacaoService service;

    @BeforeEach
    void setup() {
        repository = mock(AvaliacaoRepository.class);
        service = new AvaliacaoService(repository);
    }

    @Test
    void testCadastrarNotaValida() {
        Avaliacao avaliacao = new Avaliacao("12345678901", "historico1", 4, "Comentário", "loja1");

        when(repository.salvar(avaliacao)).thenReturn(avaliacao);

        Avaliacao resultado = service.cadastrar(avaliacao);

        assertEquals(avaliacao, resultado);
        verify(repository).salvar(avaliacao);
    }

    @Test
    void testCadastrarNotaMenorQueUm_LancaExcecao() {
        Avaliacao avaliacao = new Avaliacao("12345678901", "historico1", 0, "Comentário", "loja1");

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> service.cadastrar(avaliacao));

        assertEquals("Nota deve ser entre 1 e 5.", ex.getMessage());
        verify(repository, never()).salvar(any());
    }

    @Test
    void testCadastrarNotaMaiorQueCinco_LancaExcecao() {
        Avaliacao avaliacao = new Avaliacao("12345678901", "historico1", 6, "Comentário", "loja1");

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> service.cadastrar(avaliacao));

        assertEquals("Nota deve ser entre 1 e 5.", ex.getMessage());
        verify(repository, never()).salvar(any());
    }

    @Test
    void testListarPorCompra_RetornaLista() {
        String historicoId = "historico1";
        List<Avaliacao> avaliacoes = Arrays.asList(
                new Avaliacao("123", historicoId, 4, "Bom", "loja1"),
                new Avaliacao("456", historicoId, 5, "Ótimo", "loja1")
        );

        when(repository.listarPorCompra(historicoId)).thenReturn(avaliacoes);

        List<Avaliacao> resultado = service.listarPorCompra(historicoId);

        assertEquals(2, resultado.size());
        assertTrue(resultado.containsAll(avaliacoes));
    }

    @Test
    void testListarPorCompra_ListaVazia() {
        String historicoId = "compraInexistente";

        when(repository.listarPorCompra(historicoId)).thenReturn(Collections.emptyList());

        List<Avaliacao> resultado = service.listarPorCompra(historicoId);

        assertTrue(resultado.isEmpty());
    }

    @Test
    void testCalcularMediaAvaliacoesLoja_Ruim() {
        String lojaId = "lojaRuim";

        when(repository.calcularMediaAvaliacoesPorLoja(lojaId)).thenReturn(1.8);

        String resultado = service.calcularMediaAvaliacoesLoja(lojaId);

        assertEquals("1,8 - Ruim", resultado);
    }

    @Test
    void testCalcularMediaAvaliacoesLoja_Media() {
        String lojaId = "lojaMedia";

        when(repository.calcularMediaAvaliacoesPorLoja(lojaId)).thenReturn(2.5);

        String resultado = service.calcularMediaAvaliacoesLoja(lojaId);

        assertEquals("2,5 - Média", resultado);
    }

    @Test
    void testCalcularMediaAvaliacoesLoja_Boa() {
        String lojaId = "lojaBoa";

        when(repository.calcularMediaAvaliacoesPorLoja(lojaId)).thenReturn(3.7);

        String resultado = service.calcularMediaAvaliacoesLoja(lojaId);

        assertEquals("3,7 - Boa", resultado);
    }

    @Test
    void testCalcularMediaAvaliacoesLoja_Otima() {
        String lojaId = "lojaOtima";

        when(repository.calcularMediaAvaliacoesPorLoja(lojaId)).thenReturn(4.9);

        String resultado = service.calcularMediaAvaliacoesLoja(lojaId);

        assertEquals("4,9 - Ótima", resultado);
    }

    @Test
    void testCalcularMediaAvaliacoesLoja_Arredondamento() {
        String lojaId = "lojaArredonda";

        when(repository.calcularMediaAvaliacoesPorLoja(lojaId)).thenReturn(3.666);

        String resultado = service.calcularMediaAvaliacoesLoja(lojaId);

        assertEquals("3,7 - Boa", resultado);
    }

    @Test
    void testCalcularMediaAvaliacoesLoja_ZeroMedia() {
        String lojaId = "lojaSemAvaliacoes";

        when(repository.calcularMediaAvaliacoesPorLoja(lojaId)).thenReturn(0.0);

        String resultado = service.calcularMediaAvaliacoesLoja(lojaId);

        assertEquals("0,0 - Ruim", resultado);
    }
}
