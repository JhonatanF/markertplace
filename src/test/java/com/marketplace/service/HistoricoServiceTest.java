package com.marketplace.service;

import com.marketplace.model.Historico;
import com.marketplace.model.Historico.Compra;
import com.marketplace.model.Produto;
import com.marketplace.repository.HistoricoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.File;
import java.lang.reflect.Field;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class HistoricoServiceTest {

    @TempDir
    Path tempDir;

    private HistoricoRepository repository;
    private HistoricoService service;

    private void setRepositoryPath(HistoricoRepository repo, String path) throws Exception {
        Field pathField = HistoricoRepository.class.getDeclaredField("path");
        pathField.setAccessible(true);
        pathField.set(repo, path);
    }

    @BeforeEach
    void setUp() throws Exception {
        repository = new HistoricoRepository(Historico.class);
        File tempFile = tempDir.resolve("historicos-temp.json").toFile();
        setRepositoryPath(repository, tempFile.getAbsolutePath());

        service = new HistoricoService(repository);
        List<Historico> historico = new ArrayList<>();

        // Limpa o arquivo de dados (salva lista vazia)
        repository.salvarTodos(historico);
    }

    private Produto criarProdutoExemplo(String id, String nome, double valor) {
        Produto p = new Produto();
        p.setId(id);
        p.setNome(nome);
        p.setValor(valor);
        return p;
    }

    @Test
    void testBuscarHistoricoExistente() {
        String cpf = "12345678901";

        Historico historico = new Historico(cpf);
        repository.salvar(historico);

        Historico encontrado = service.buscarPorComprador(cpf);
        assertNotNull(encontrado);
        assertEquals(cpf, encontrado.getCompradorCpf());
        assertTrue(encontrado.getCompras().isEmpty());
    }

    @Test
    void testBuscarHistoricoInexistente_CriaNovo() {
        String cpf = "cpf-inexistente";

        Historico novoHistorico = service.buscarPorComprador(cpf);

        assertNotNull(novoHistorico);
        assertEquals(cpf, novoHistorico.getCompradorCpf());
        assertTrue(novoHistorico.getCompras().isEmpty());

        Optional<Historico> opt = repository.buscarPorComprador(cpf);
        assertTrue(opt.isPresent());
    }

    @Test
    void testCadastrarOuAtualizarHistorico_ComprasNovas() {
        String cpf = "12345678901";

        Historico historico = new Historico(cpf);

        // Adiciona uma compra existente
        List<Produto> produtosCompra1 = List.of(
                criarProdutoExemplo("p1", "Produto 1", 10.0),
                criarProdutoExemplo("p2", "Produto 2", 20.0)
        );
        historico.adicionarCompra(produtosCompra1, 30.0);

        repository.salvar(historico);

        // Novas compras para adicionar
        List<Compra> novasCompras = new ArrayList<>();

        List<Produto> produtosCompra2 = List.of(
                criarProdutoExemplo("p3", "Produto 3", 15.0)
        );
        Compra compra2 = new Compra(produtosCompra2, 15.0);

        List<Produto> produtosCompra3 = List.of(
                criarProdutoExemplo("p4", "Produto 4", 40.0),
                criarProdutoExemplo("p5", "Produto 5", 60.0)
        );
        Compra compra3 = new Compra(produtosCompra3, 100.0);

        novasCompras.add(compra2);
        novasCompras.add(compra3);

        Historico atualizado = service.cadastrarOuAtualizarHistorico(cpf, novasCompras);

        assertNotNull(atualizado);
        assertEquals(cpf, atualizado.getCompradorCpf());

        // Agora devem existir 3 compras no total
        assertEquals(3, atualizado.getCompras().size());

        // Verifica se compras novas estão presentes pelo id dos produtos
        boolean encontrouCompra2 = atualizado.getCompras().stream()
                .anyMatch(c -> c.getProdutos().stream().anyMatch(p -> p.getId().equals("p3")));
        assertTrue(encontrouCompra2);

        boolean encontrouCompra3 = atualizado.getCompras().stream()
                .anyMatch(c -> c.getProdutos().stream().anyMatch(p -> p.getId().equals("p4")));
        assertTrue(encontrouCompra3);
    }

    @Test
    void testCadastrarOuAtualizarHistorico_NovoHistorico() {
        String cpf = "novo-cpf";

        List<Produto> produtos = List.of(
                criarProdutoExemplo("prod1", "Produto 1", 50.0)
        );

        Compra compra = new Compra(produtos, 50.0);

        List<Compra> compras = List.of(compra);

        Historico historico = service.cadastrarOuAtualizarHistorico(cpf, compras);

        assertNotNull(historico);
        assertEquals(cpf, historico.getCompradorCpf());
        assertEquals(1, historico.getCompras().size());
        assertEquals("prod1", historico.getCompras().get(0).getProdutos().get(0).getId());

        Optional<Historico> salvo = repository.buscarPorComprador(cpf);
        assertTrue(salvo.isPresent());
        assertEquals(1, salvo.get().getCompras().size());
    }

    @Test
    void testListarTodos() {
        Historico h1 = new Historico("cpf1");
        Historico h2 = new Historico("cpf2");
        repository.salvar(h1);
        repository.salvar(h2);

        List<Historico> todos = service.listarTodos();
        assertEquals(2, todos.size());
        assertTrue(todos.stream().anyMatch(h -> "cpf1".equals(h.getCompradorCpf())));
        assertTrue(todos.stream().anyMatch(h -> "cpf2".equals(h.getCompradorCpf())));
    }

    @Test
    void testCadastrarOuAtualizarHistorico_ComprasVazias() {
        String cpf = "12345678901";

        Historico historico = service.cadastrarOuAtualizarHistorico(cpf, new ArrayList<>());

        assertNotNull(historico);
        assertEquals(cpf, historico.getCompradorCpf());
        assertTrue(historico.getCompras().isEmpty());
    }
}
