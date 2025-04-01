package com.marketplace.repository;

import com.marketplace.model.Comprador;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import java.util.List;
import java.util.Optional;

public class CompradorRepositoryTest {

    private CompradorRepository compradorRepository;
    private Comprador comprador;

    @BeforeEach
    void setUp() {
        // Inicializa o repositório de compradores
        compradorRepository = new CompradorRepository();

        // Cria um novo comprador usando o construtor da classe Comprador
        comprador = new Comprador("Comprador Teste", "comprador@teste.com", "senha123", "12345678901", "Avenida Teste, 456");
    }

    @Test
    void testSaveAndFindById() {
        // Testa salvar um comprador e encontrá-lo pelo CPF
        compradorRepository.save(comprador);

        Comprador foundComprador = compradorRepository.findById(comprador.getCpf()).orElse(null);

        assertNotNull(foundComprador, "Comprador deveria ser encontrado.");
        assertEquals(comprador.getNome(), foundComprador.getNome(), "Os nomes dos compradores devem ser iguais.");
        assertEquals(comprador.getEmail(), foundComprador.getEmail(), "Os e-mails dos compradores devem ser iguais.");
        assertEquals(comprador.getEndereco(), foundComprador.getEndereco(), "Os endereços dos compradores devem ser iguais.");
        assertEquals(comprador.getCpf(), foundComprador.getCpf(), "Os CPFs dos compradores devem ser iguais.");
        assertEquals(comprador.getPontuacao(), foundComprador.getPontuacao(), "As pontuações dos compradores devem ser iguais.");
    }

    @Test
    void testFindAll() {
        // Testa a recuperação de todos os compradores
        compradorRepository.save(comprador);

        List<Comprador> compradores = compradorRepository.findAll();
        assertFalse(compradores.isEmpty(), "A lista de compradores não deve estar vazia.");
        assertTrue(compradores.contains(comprador), "Comprador deveria estar na lista.");
    }

    @Test
    void testDelete() {
        // Testa a exclusão de um comprador
        compradorRepository.save(comprador);
        assertTrue(compradorRepository.exists(comprador.getCpf()), "Comprador deveria existir antes de ser excluído.");

        compradorRepository.delete(comprador.getCpf());
        assertFalse(compradorRepository.exists(comprador.getCpf()), "Comprador não deveria existir após exclusão.");
    }

    @Test
    void testExists() {
        // Testa a verificação de existência de um comprador
        assertFalse(compradorRepository.exists(comprador.getCpf()), "Comprador não deveria existir antes de ser salvo.");

        compradorRepository.save(comprador);
        assertTrue(compradorRepository.exists(comprador.getCpf()), "Comprador deveria existir após ser salvo.");
    }

    @Test
    void testFindByNonExistentId() {
        // Testa a busca por um CPF inexistente
        Optional<Comprador> foundComprador = compradorRepository.findById("99999999999");
        assertFalse(foundComprador.isPresent(), "Comprador com CPF inexistente não deveria ser encontrado.");
    }
}
