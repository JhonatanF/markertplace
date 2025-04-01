package com.marketplace.service;

import com.marketplace.model.Admin;
import com.marketplace.repository.AdminRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.nio.file.Path;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class AdminServiceTest {

    @TempDir
    Path tempDir;

    private AdminRepository repository;
    private AdminService service;
    private Admin admin;

    @BeforeEach
    void setUp() {
        repository = new AdminRepository();
        service = new AdminService(repository);
        admin = new Admin("Admin Teste", "admin@teste.com", "senha123", "12345678901", "Rua Teste, 123");
        service.removerAdmins();
    }

    @Test
    void testCadastrarAdmin() {
        Admin cadastrado = service.cadastrar(admin);

        assertEquals(admin.getNome(), cadastrado.getNome());
        assertEquals(admin.getEmail(), cadastrado.getEmail());
        assertEquals(admin.getSenha(), cadastrado.getSenha());
        assertEquals(admin.getCpf(), cadastrado.getCpf());
        assertEquals(admin.getEndereco(), cadastrado.getEndereco());

        Optional<Admin> encontrado = service.buscarPorId(admin.getCpf());
        assertTrue(encontrado.isPresent());
    }

    @Test
    void testBuscarAdmin() {
        service.cadastrar(admin);

        Optional<Admin> encontrado = service.buscarPorId(admin.getCpf());
        assertTrue(encontrado.isPresent());
        assertEquals(admin.getNome(), encontrado.get().getNome());
    }

    @Test
    void testListarAdmins() {
        service.cadastrar(admin);

        Admin admin2 = new Admin("Outro Admin", "outro@email.com", "senha456", "98765432101", "Rua Outra, 456");
        service.cadastrar(admin2);

        List<Admin> admins = service.listarTodos();
        assertEquals(2, admins.size());
    }

    @Test
    void testAtualizarAdmin() {
        service.cadastrar(admin);

        admin.setNome("Nome Atualizado");
        admin.setEmail("novo@email.com");
        admin.setSenha("novaSenha123");
        admin.setEndereco("Novo Endereço");

        Admin atualizado = service.atualizar(admin);

        assertEquals("Nome Atualizado", atualizado.getNome());
        assertEquals("novo@email.com", atualizado.getEmail());
        assertEquals("novaSenha123", atualizado.getSenha());
        assertEquals("Novo Endereço", atualizado.getEndereco());
    }

    @Test
    void testRemoverAdmin() {
        service.cadastrar(admin);

        assertTrue(service.buscarPorId(admin.getCpf()).isPresent());
        service.remover(admin.getCpf());
        assertFalse(service.buscarPorId(admin.getCpf()).isPresent());
    }

    @Test
    void testAtualizarAdminInexistente() {
        Admin adminInexistente = new Admin("Nome", "email@test.com", "senha", "cpf-inexistente", "endereco");
        assertThrows(IllegalArgumentException.class, () ->
                service.atualizar(adminInexistente)
        );
    }

    @Test
    void testCadastrarAdminJaExistente() {
        service.cadastrar(admin);

        assertThrows(IllegalArgumentException.class, () ->
                service.cadastrar(admin)
        );
    }

    @Test
    void testRemoverAdminInexistente() {
        assertThrows(IllegalArgumentException.class, () ->
                service.remover("cpf-inexistente")
        );
    }

    @Test
    void testRemoverTodosAdmins() {
        service.cadastrar(admin);
        Admin admin2 = new Admin("Outro Admin", "outro@email.com", "senha456", "98765432101", "Rua Outra, 456");
        service.cadastrar(admin2);

        service.removerAdmins();
        assertTrue(service.listarTodos().isEmpty());
    }
}
