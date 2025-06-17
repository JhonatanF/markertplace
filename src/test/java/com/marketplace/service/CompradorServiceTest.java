package com.marketplace.service;

import com.marketplace.model.Comprador;
import com.marketplace.repository.CompradorRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.nio.file.Path;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class CompradorServiceTest {

    @TempDir
    Path tempDir;

    private CompradorRepository repository;
    private CompradorService service;
    private Comprador comprador;

    @BeforeEach
    void setUp() {
        repository = new CompradorRepository();
        service = new CompradorService(repository);
        comprador = new Comprador("Comprador Teste", "comprador@teste.com", "senha123",
                "12345678901", "Rua Teste, 123");
        service.removerCompradores();  // limpa o repositório antes de cada teste
    }

    @Test
    void testCadastrarComprador() {
        Comprador cadastrado = service.cadastrar(comprador);

        assertEquals(comprador.getNome(), cadastrado.getNome());
        assertEquals(comprador.getEmail(), cadastrado.getEmail());
        assertEquals(comprador.getSenha(), cadastrado.getSenha());
        assertEquals(comprador.getCpf(), cadastrado.getCpf());
        assertEquals(comprador.getEndereco(), cadastrado.getEndereco());

        Optional<Comprador> encontrado = service.buscarPorId(comprador.getCpf());
        assertTrue(encontrado.isPresent());
    }

    @Test
    void testCadastrarCompradorJaExistente_LancaExcecao() {
        service.cadastrar(comprador);

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () ->
                service.cadastrar(comprador)
        );
        assertEquals("Comprador já cadastrado com este CPF", ex.getMessage());
    }

    @Test
    void testBuscarCompradorExistente() {
        service.cadastrar(comprador);

        Optional<Comprador> encontrado = service.buscarPorId(comprador.getCpf());
        assertTrue(encontrado.isPresent());
        assertEquals(comprador.getNome(), encontrado.get().getNome());
    }

    @Test
    void testBuscarCompradorInexistente() {
        Optional<Comprador> encontrado = service.buscarPorId("cpf-inexistente");
        assertFalse(encontrado.isPresent());
    }

    @Test
    void testListarCompradores() {
        service.cadastrar(comprador);

        Comprador comprador2 = new Comprador("Outro Comprador", "outro@email.com", "senha456",
                "98765432101", "Rua Outra, 456");
        service.cadastrar(comprador2);

        List<Comprador> compradores = service.listarTodos();
        assertEquals(2, compradores.size());
        assertTrue(compradores.stream().anyMatch(c -> c.getCpf().equals(comprador.getCpf())));
        assertTrue(compradores.stream().anyMatch(c -> c.getCpf().equals(comprador2.getCpf())));
    }

    @Test
    void testAtualizarCompradorExistente() {
        service.cadastrar(comprador);

        comprador.setNome("Nome Atualizado");
        comprador.setEmail("novo@email.com");
        comprador.setSenha("novaSenha123");
        comprador.setEndereco("Novo Endereço");

        Comprador atualizado = service.atualizar(comprador);

        assertEquals("Nome Atualizado", atualizado.getNome());
        assertEquals("novo@email.com", atualizado.getEmail());
        assertEquals("novaSenha123", atualizado.getSenha());
        assertEquals("Novo Endereço", atualizado.getEndereco());
    }

    @Test
    void testAtualizarCompradorInexistente_LancaExcecao() {
        Comprador compradorInexistente = new Comprador("Nome", "email@test.com", "senha", "cpf-inexistente", "endereco");

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () ->
                service.atualizar(compradorInexistente)
        );
        assertEquals("Comprador não encontrado", ex.getMessage());
    }

    @Test
    void testRemoverCompradorExistente() {
        service.cadastrar(comprador);

        assertTrue(service.buscarPorId(comprador.getCpf()).isPresent());
        service.remover(comprador.getCpf());
        assertFalse(service.buscarPorId(comprador.getCpf()).isPresent());
    }

    @Test
    void testRemoverCompradorInexistente_LancaExcecao() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () ->
                service.remover("cpf-inexistente")
        );
        assertEquals("Comprador não encontrado", ex.getMessage());
    }

    @Test
    void testRemoverTodosCompradores() {
        Comprador comprador2 = new Comprador("Outro Comprador", "outro@email.com", "senha456",
                "98765432101", "Rua Outra, 456");

        service.cadastrar(comprador);
        service.cadastrar(comprador2);

        List<Comprador> todosAntes = service.listarTodos();
        assertEquals(2, todosAntes.size());

        service.removerCompradores();

        List<Comprador> todosDepois = service.listarTodos();
        assertTrue(todosDepois.isEmpty());
    }
}
