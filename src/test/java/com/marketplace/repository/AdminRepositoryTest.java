package com.marketplace.repository;

import com.marketplace.model.Admin;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import java.util.List;
import java.util.Optional;

public class AdminRepositoryTest {

    private AdminRepository adminRepository;
    private Admin admin;

    @BeforeEach
    void setUp() {
        // Inicializa o repositório de administradores
        adminRepository = new AdminRepository();

        // Cria um novo administrador usando o construtor da classe Admin
        admin = new Admin("Admin Teste", "admin@teste.com", "senha123", "98765432100", "Rua Admin, 123");
    }

    @Test
    void testSaveAndFindById() {
        // Testa salvar um administrador e encontrá-lo pelo CPF
        adminRepository.save(admin);

        Admin foundAdmin = adminRepository.findById(admin.getCpf()).orElse(null);

        assertNotNull(foundAdmin, "Administrador deveria ser encontrado.");
        assertEquals(admin.getNome(), foundAdmin.getNome(), "Os nomes dos administradores devem ser iguais.");
        assertEquals(admin.getEmail(), foundAdmin.getEmail(), "Os e-mails dos administradores devem ser iguais.");
        assertEquals(admin.getEndereco(), foundAdmin.getEndereco(), "Os endereços dos administradores devem ser iguais.");
        assertEquals(admin.getCpf(), foundAdmin.getCpf(), "Os CPFs dos administradores devem ser iguais.");
    }

    @Test
    void testFindAll() {
        // Testa a recuperação de todos os administradores
        adminRepository.save(admin);

        List<Admin> admins = adminRepository.findAll();
        assertFalse(admins.isEmpty(), "A lista de administradores não deve estar vazia.");
        assertTrue(admins.contains(admin), "Administrador deveria estar na lista.");
    }

    @Test
    void testDelete() {
        // Testa a exclusão de um administrador
        adminRepository.save(admin);
        assertTrue(adminRepository.exists(admin.getCpf()), "Administrador deveria existir antes de ser excluído.");

        adminRepository.delete(admin.getCpf());
        assertFalse(adminRepository.exists(admin.getCpf()), "Administrador não deveria existir após exclusão.");
    }

    @Test
    void testExists() {
        // Testa a verificação de existência de um administrador
        assertFalse(adminRepository.exists(admin.getCpf()), "Administrador não deveria existir antes de ser salvo.");

        adminRepository.save(admin);
        assertTrue(adminRepository.exists(admin.getCpf()), "Administrador deveria existir após ser salvo.");
    }

    @Test
    void testFindByNonExistentId() {
        // Testa a busca por um CPF inexistente
        Optional<Admin> foundAdmin = adminRepository.findById("11122233344");
        assertFalse(foundAdmin.isPresent(), "Administrador com CPF inexistente não deveria ser encontrado.");
    }
}
