package com.marketplace.repository;

import com.marketplace.model.Loja;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import java.util.List;
import java.util.Optional;

public class LojaRepositoryTest {

    private LojaRepository lojaRepository;
    private Loja loja;

    @BeforeEach
    void setUp() {
        // Inicializa o repositório de lojas
        lojaRepository = new LojaRepository();

        // Cria uma nova loja usando o construtor da classe Loja
        loja = new Loja("Loja Teste", "loja@teste.com", "senha123", "12345678901234", "Rua Teste, 123");
    }

    @Test
    void testSaveAndFindById() {
        // Testa salvar uma loja e encontrá-la pelo CPF/CNPJ
        lojaRepository.save(loja);

        Loja foundLoja = lojaRepository.findById(loja.getCpfCnpj()).orElse(null);

        assertNotNull(foundLoja, "Loja deveria ser encontrada.");
        assertEquals(loja.getNome(), foundLoja.getNome(), "Os nomes das lojas devem ser iguais.");
        assertEquals(loja.getEmail(), foundLoja.getEmail(), "Os e-mails das lojas devem ser iguais.");
        assertEquals(loja.getEndereco(), foundLoja.getEndereco(), "Os endereços das lojas devem ser iguais.");
        assertEquals(loja.getCpfCnpj(), foundLoja.getCpfCnpj(), "Os CNPJs das lojas devem ser iguais.");
    }

    @Test
    void testFindAll() {
        // Testa a recuperação de todas as lojas
        lojaRepository.save(loja);

        List<Loja> lojas = lojaRepository.findAll();
        assertFalse(lojas.isEmpty(), "A lista de lojas não deve estar vazia.");
        assertTrue(lojas.contains(loja), "Loja deveria estar na lista.");
    }

    @Test
    void testDelete() {
        // Testa a exclusão de uma loja
        lojaRepository.save(loja);
        assertTrue(lojaRepository.exists(loja.getCpfCnpj()), "Loja deveria existir antes de ser excluída.");

        lojaRepository.delete(loja.getCpfCnpj());
        assertFalse(lojaRepository.exists(loja.getCpfCnpj()), "Loja não deveria existir após exclusão.");
    }

    @Test
    void testExists() {
        // Testa a verificação de existência de uma loja
        assertFalse(lojaRepository.exists(loja.getCpfCnpj()), "Loja não deveria existir antes de ser salva.");

        lojaRepository.save(loja);
        assertTrue(lojaRepository.exists(loja.getCpfCnpj()), "Loja deveria existir após ser salva.");
    }

    @Test
    void testFindByNonExistentId() {
        // Testa a busca por um ID (CPF/CNPJ) inexistente
        Optional<Loja> foundLoja = lojaRepository.findById("99999999999999");
        assertFalse(foundLoja.isPresent(), "Loja com CPF/CNPJ inexistente não deveria ser encontrada.");
    }
}
