package com.marketplace.repository;

import com.marketplace.model.Loja;
import com.marketplace.utils.TestDataLoader;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class LojaRepositoryTest {
    private LojaRepository repository;
    private Loja loja;

    @BeforeEach
    void setUp() {
        repository = new LojaRepository();
        loja = TestDataLoader.loadLojas().get(0);
    }



    @Test
    void testSaveAndFind() {
        repository.save(loja);
        Optional<Loja> found = repository.findById(loja.getCpfCnpj());
        
        assertTrue(found.isPresent());
        assertEquals(loja.getNome(), found.get().getNome());
        assertEquals(loja.getEmail(), found.get().getEmail());
        assertEquals(loja.getSenha(), found.get().getSenha());
        assertEquals(loja.getEndereco(), found.get().getEndereco());
    }

    @Test
    void testFindNonExistent() {
        Optional<Loja> found = repository.findById("cpf-inexistente");
        assertFalse(found.isPresent());
    }

    @Test
    void testFindAll() {
        List<Loja> lojas = TestDataLoader.loadLojas();
        lojas.forEach(l -> repository.save(l));

        List<Loja> foundLojas = repository.findAll();
        assertEquals(lojas.size(), foundLojas.size());
        lojas.forEach(l -> 
            assertTrue(foundLojas.stream()
                .anyMatch(fl -> fl.getCpfCnpj().equals(l.getCpfCnpj()))));
    }

    @Test
    void testFindAllEmpty() {
        List<Loja> lojas = repository.findAll();
        assertTrue(lojas.isEmpty());
    }

    @Test
    void testDelete() {
        repository.save(loja);
        assertTrue(repository.findById(loja.getCpfCnpj()).isPresent());
        
        repository.delete(loja.getCpfCnpj());
        assertFalse(repository.findById(loja.getCpfCnpj()).isPresent());
    }

    @Test
    void testDeleteNonExistent() {
        repository.delete("cpf-inexistente"); // Should not throw exception
    }

    @Test
    void testUpdate() {
        repository.save(loja);
        
        loja.setNome("Nome Atualizado");
        loja.setEmail("novo@email.com");
        repository.save(loja);
        
        Optional<Loja> updated = repository.findById(loja.getCpfCnpj());
        assertTrue(updated.isPresent());
        assertEquals("Nome Atualizado", updated.get().getNome());
        assertEquals("novo@email.com", updated.get().getEmail());
    }

    @Test
    void testExists() {
        assertFalse(repository.exists(loja.getCpfCnpj()));
        repository.save(loja);
        assertTrue(repository.exists(loja.getCpfCnpj()));
    }
}
