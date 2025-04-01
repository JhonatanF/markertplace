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
        service.removerCompradores();
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
    void testBuscarComprador() {
        service.cadastrar(comprador);
        
        Optional<Comprador> encontrado = service.buscarPorId(comprador.getCpf());
        assertTrue(encontrado.isPresent());
        assertEquals(comprador.getNome(), encontrado.get().getNome());
    }

    @Test
    void testListarCompradores() {
        service.cadastrar(comprador);
        
        Comprador comprador2 = new Comprador("Outro Comprador", "outro@email.com", "senha456", 
                                           "98765432101", "Rua Outra, 456");
        service.cadastrar(comprador2);

        List<Comprador> compradores = service.listarTodos();
        assertEquals(2, compradores.size());
    }

    @Test
    void testAtualizarComprador() {
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
    void testRemoverComprador() {
        service.cadastrar(comprador);
        
        assertTrue(service.buscarPorId(comprador.getCpf()).isPresent());
        service.remover(comprador.getCpf());
        assertFalse(service.buscarPorId(comprador.getCpf()).isPresent());
    }

    @Test
    void testAtualizarCompradorInexistente() {
        Comprador compradorInexistente = new Comprador("Nome", "email@test.com", "senha", "cpf-inexistente", "endereco");
        assertThrows(IllegalArgumentException.class, () -> 
            service.atualizar(compradorInexistente)
        );
    }

    @Test
    void testCadastrarCompradorJaExistente() {
        service.cadastrar(comprador);

        assertThrows(IllegalArgumentException.class, () ->
            service.cadastrar(comprador)
        );
    }
}
