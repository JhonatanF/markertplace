package com.marketplace.repository;

import com.marketplace.model.Historico;
import com.marketplace.model.Produto;
import org.junit.jupiter.api.*;

import java.io.File;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class HistoricoRepositoryTest {

    private HistoricoRepository historicoRepository;
    private final String path = "historicos-v1.json";

    private Produto produto1;
    private Produto produto2;
    private List<Produto> produtos;

    @BeforeEach
    void setUp() {
        // Remove o arquivo para ambiente limpo de teste
        File file = new File(path);
        if (file.exists()) file.delete();

        historicoRepository = new HistoricoRepository(Historico.class);

        produto1 = new Produto("Produto A", 29.90, "Eletrônico", 1, "MarcaX", "Produto A descrição", "12345678900");
        produto2 = new Produto("Produto B", 39.90, "Casa", 2, "MarcaY", "Produto B descrição", "12345678900");
        produtos = List.of(produto1, produto2);
    }

    @Test
    void testSalvarHistoricoComUmaCompra() {
        Historico historico = new Historico("11111111111");
        historico.adicionarCompra(produtos, 69.80);

        historicoRepository.salvar(historico);

        Optional<Historico> encontrado = historicoRepository.buscarPorComprador("11111111111");
        assertTrue(encontrado.isPresent());

        Historico h = encontrado.get();
        assertEquals("11111111111", h.getCompradorCpf());
        assertEquals(1, h.getCompras().size());

        Historico.Compra compra = h.getCompras().get(0);
        assertEquals(69.80, compra.getTotal(), 0.01);
        assertEquals(2, compra.getProdutos().size());

        Produto p1 = compra.getProdutos().get(0);
        assertEquals("Produto A", p1.getNome());
        assertEquals("Eletrônico", p1.getTipo());
        assertEquals("MarcaX", p1.getMarca());
    }

    @Test
    void testSubstituirHistoricoExistente() {
        Historico h1 = new Historico("22222222222");
        h1.adicionarCompra(List.of(produto1), 29.90);
        historicoRepository.salvar(h1);

        Historico h2 = new Historico("22222222222");
        h2.adicionarCompra(List.of(produto2), 39.90);
        historicoRepository.salvar(h2);

        List<Historico> todos = historicoRepository.listarTodos();
        assertEquals(1, todos.size());

        Historico resultado = todos.get(0);
        assertEquals("22222222222", resultado.getCompradorCpf());
        assertEquals(1, resultado.getCompras().size());

        Produto p = resultado.getCompras().get(0).getProdutos().get(0);
        assertEquals("Produto B", p.getNome());
    }

    @Test
    void testBuscarHistoricoInexistente() {
        Optional<Historico> notFound = historicoRepository.buscarPorComprador("00000000000");
        assertFalse(notFound.isPresent());
    }

    @Test
    void testListarTodosHistoricos() {
        Historico h1 = new Historico("33333333333");
        h1.adicionarCompra(produtos, 100.0);
        Historico h2 = new Historico("44444444444");
        h2.adicionarCompra(List.of(produto1), 29.90);

        historicoRepository.salvar(h1);
        historicoRepository.salvar(h2);

        List<Historico> historicos = historicoRepository.listarTodos();
        assertEquals(2, historicos.size());

        Set<String> cpfs = new HashSet<>();
        historicos.forEach(h -> cpfs.add(h.getCompradorCpf()));

        assertTrue(cpfs.contains("33333333333"));
        assertTrue(cpfs.contains("44444444444"));
    }

    @Test
    void testSalvarHistoricoComDataDeCompra() {
        Historico historico = new Historico("55555555555");
        historico.adicionarCompra(List.of(produto1), 29.90);

        historicoRepository.salvar(historico);

        Optional<Historico> encontrado = historicoRepository.buscarPorComprador("55555555555");
        assertTrue(encontrado.isPresent());

        Historico.Compra compra = encontrado.get().getCompras().get(0);
        assertNotNull(compra.getData());
        assertTrue(compra.getData().before(new Date(System.currentTimeMillis() + 2000)));
    }

    @AfterEach
    void cleanUp() {
        File file = new File(path);
        if (file.exists()) file.delete();
    }
}
