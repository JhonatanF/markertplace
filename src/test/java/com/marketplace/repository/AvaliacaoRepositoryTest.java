package com.marketplace.repository;

import com.marketplace.model.Avaliacao;
import org.junit.jupiter.api.*;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class AvaliacaoRepositoryTest {

    private AvaliacaoRepository repository;
    private static final String TEST_FILE = "avaliacoes-v1.json";

    private Avaliacao avaliacao1;
    private Avaliacao avaliacao2;

    @BeforeEach
    void setUp() {
        // Limpa o arquivo antes de cada teste
        File file = new File(TEST_FILE);
        if (file.exists()) file.delete();

        repository = new AvaliacaoRepository();

        // Criação de avaliações de teste
        avaliacao1 = new Avaliacao("12345678900", "hist1", 4, "Boa compra", "loja1");
        avaliacao2 = new Avaliacao("12345678900", "hist1", 5, "Excelente serviço", "loja1");
    }

    @Test
    void testSalvarEAtribuirId() {
        Avaliacao salva = repository.salvar(avaliacao1);
        assertNotNull(salva.getId(), "ID deve ser atribuído automaticamente");
        assertEquals("hist1", salva.getHistoricoId());
        assertEquals(4, salva.getNota());
        assertEquals("12345678900", salva.getCompradorCpf());
        assertEquals("loja1", salva.getLojaId());
        assertEquals("Boa compra", salva.getComentario());
    }

    @Test
    void testBuscarPorId() {
        Avaliacao salva = repository.salvar(avaliacao1);
        Optional<Avaliacao> resultado = repository.buscarPorId(salva.getId());

        assertTrue(resultado.isPresent());
        assertEquals(salva.getNota(), resultado.get().getNota());
        assertEquals(salva.getComentario(), resultado.get().getComentario());
    }

    @Test
    void testBuscarPorIdInexistente() {
        Optional<Avaliacao> resultado = repository.buscarPorId("id_invalido");
        assertFalse(resultado.isPresent());
    }

    @Test
    void testListarPorCompra() {
        repository.salvar(avaliacao1);
        repository.salvar(avaliacao2);

        List<Avaliacao> lista = repository.listarPorCompra("hist1");
        assertEquals(2, lista.size());
        assertTrue(lista.stream().allMatch(a -> a.getHistoricoId().equals("hist1")));
    }

    @Test
    void testListarPorCompraSemResultados() {
        List<Avaliacao> lista = repository.listarPorCompra("hist_nao_existe");
        assertNotNull(lista);
        assertTrue(lista.isEmpty());
    }

    @Test
    void testCalcularMediaAvaliacoesPorLoja() {
        repository.salvar(avaliacao1);
        repository.salvar(avaliacao2);

        double media = repository.calcularMediaAvaliacoesPorLoja("loja1");
        assertEquals(4.5, media, 0.01);
    }

    @Test
    void testCalcularMediaSemAvaliacoes() {
        double media = repository.calcularMediaAvaliacoesPorLoja("loja_inexistente");
        assertEquals(0.0, media);
    }

    @Test
    void testPersistenciaNoArquivo() {
        repository.salvar(avaliacao1);

        // Nova instância deve carregar os dados
        AvaliacaoRepository novoRepo = new AvaliacaoRepository();
        List<Avaliacao> lista = novoRepo.listarPorCompra("hist1");

        assertEquals(1, lista.size());
        Avaliacao carregada = lista.get(0);
        assertEquals("Boa compra", carregada.getComentario());
        assertEquals(4, carregada.getNota());
    }

    @Test
    void testArquivoVazioNaoLancaErro() throws IOException {
        // Cria um arquivo vazio
        try (FileWriter writer = new FileWriter(TEST_FILE)) {
            writer.write("");
        }

        assertDoesNotThrow(AvaliacaoRepository::new);
    }

    @AfterEach
    void tearDown() {
        File file = new File(TEST_FILE);
        if (file.exists()) file.delete();
    }
}
