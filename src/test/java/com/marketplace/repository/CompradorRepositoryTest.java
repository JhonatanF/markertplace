package com.marketplace.repository;

import com.marketplace.model.Comprador;
import com.marketplace.utils.TestDataLoader;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class CompradorRepositoryTest {
    private CompradorRepository repository;
    private Comprador comprador;

    @BeforeEach
    void setUp() {
        repository = new CompradorRepository();
        comprador = TestDataLoader.loadCompradores().get(0);
    }



    @Test
    void testSaveAndFind() {
        repository.save(comprador);
        Optional<Comprador> found = repository.findById(comprador.getCpf());
        
        assertTrue(found.isPresent());
        assertEquals(comprador.getNome(), found.get().getNome());
        assertEquals(comprador.getEmail(), found.get().getEmail());
        assertEquals(comprador.getSenha(), found.get().getSenha());
        assertEquals(comprador.getEndereco(), found.get().getEndereco());
    }

    @Test
    void testFindNonExistent() {
        Optional<Comprador> found = repository.findById("cpf-inexistente");
        assertFalse(found.isPresent());
    }

    @Test
    void testFindAll() {
        List<Comprador> compradores = TestDataLoader.loadCompradores();
        compradores.forEach(c -> repository.save(c));

        List<Comprador> foundCompradores = repository.findAll();
        assertEquals(compradores.size(), foundCompradores.size());
        compradores.forEach(c -> 
            assertTrue(foundCompradores.stream()
                .anyMatch(fc -> fc.getCpf().equals(c.getCpf()))));
    }

    @Test
    void testFindAllEmpty() {
        List<Comprador> compradores = repository.findAll();
        assertTrue(compradores.isEmpty());
    }

    @Test
    void testDelete() {
        repository.save(comprador);
        assertTrue(repository.findById(comprador.getCpf()).isPresent());
        
        repository.delete(comprador.getCpf());
        assertFalse(repository.findById(comprador.getCpf()).isPresent());
    }

    @Test
    void testDeleteNonExistent() {
        repository.delete("cpf-inexistente"); // Não deve lançar exceção
    }

    @Test
    void testUpdate() {
        repository.save(comprador);
        
        comprador.setNome("Nome Atualizado");
        comprador.setEmail("novo@email.com");
        repository.save(comprador);
        
        Optional<Comprador> updated = repository.findById(comprador.getCpf());
        assertTrue(updated.isPresent());
        assertEquals("Nome Atualizado", updated.get().getNome());
        assertEquals("novo@email.com", updated.get().getEmail());
    }

    @Test
    void testExists() {
        assertFalse(repository.exists(comprador.getCpf()));
        repository.save(comprador);
        assertTrue(repository.exists(comprador.getCpf()));
    }
}
