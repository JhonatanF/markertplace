package com.marketplace.repository;

import com.marketplace.model.Admin;
import org.junit.jupiter.api.*;

import java.io.File;
import java.nio.file.Files;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class AdminRepositoryTest {

    private AdminRepository adminRepository;
    private Admin admin1;
    private Admin admin2;

    private static final String TEST_FILENAME = "admins-v1.json";

    @BeforeEach
    void setUp() {
        // Limpa o arquivo antes de cada teste para garantir isolamento
        File file = new File(TEST_FILENAME);
        if (file.exists()) file.delete();

        adminRepository = new AdminRepository();
        admin1 = new Admin("Admin Um", "admin1@teste.com", "senha1", "11111111111", "Rua Um, 123");
        admin2 = new Admin("Admin Dois", "admin2@teste.com", "senha2", "22222222222", "Rua Dois, 456");
    }

    @Test
    void testSaveAndFindById() {
        adminRepository.save(admin1);

        Optional<Admin> result = adminRepository.findById(admin1.getCpf());
        assertTrue(result.isPresent());
        assertEquals(admin1.getNome(), result.get().getNome());
        assertEquals(admin1.getEmail(), result.get().getEmail());
        assertEquals(admin1.getEndereco(), result.get().getEndereco());
        assertEquals(admin1.getCpf(), result.get().getCpf());
    }

    @Test
    void testFindAll() {
        adminRepository.save(admin1);
        adminRepository.save(admin2);

        List<Admin> admins = adminRepository.findAll();
        assertEquals(2, admins.size());
        assertTrue(admins.contains(admin1));
        assertTrue(admins.contains(admin2));
    }

    @Test
    void testFindAllEmpty() {
        List<Admin> admins = adminRepository.findAll();
        assertNotNull(admins);
        assertTrue(admins.isEmpty());
    }

    @Test
    void testExists() {
        assertFalse(adminRepository.exists(admin1.getCpf()));
        adminRepository.save(admin1);
        assertTrue(adminRepository.exists(admin1.getCpf()));
    }

    @Test
    void testDelete() {
        adminRepository.save(admin1);
        assertTrue(adminRepository.exists(admin1.getCpf()));

        adminRepository.delete(admin1.getCpf());
        assertFalse(adminRepository.exists(admin1.getCpf()));
        assertFalse(adminRepository.findById(admin1.getCpf()).isPresent());
    }

    @Test
    void testDeleteNonExistent() {
        adminRepository.delete("99999999999");
        // Nenhuma exceção deve ser lançada
        assertTrue(adminRepository.findAll().isEmpty());
    }

    @Test
    void testFindByNonExistentId() {
        Optional<Admin> result = adminRepository.findById("33333333333");
        assertFalse(result.isPresent());
    }

    @Test
    void testOverwriteExistingAdmin() {
        adminRepository.save(admin1);
        Admin updated = new Admin("Admin Atualizado", "novo@teste.com", "novaSenha", admin1.getCpf(), "Nova Rua, 789");

        adminRepository.save(updated);

        Optional<Admin> result = adminRepository.findById(admin1.getCpf());
        assertTrue(result.isPresent());
        assertEquals("Admin Atualizado", result.get().getNome());
        assertEquals("novo@teste.com", result.get().getEmail());
    }

    @Test
    void testPersistenceAcrossInstances() {
        adminRepository.save(admin1);

        // Simula nova instância que deve carregar do arquivo salvo
        AdminRepository newRepo = new AdminRepository();
        Optional<Admin> reloaded = newRepo.findById(admin1.getCpf());

        assertTrue(reloaded.isPresent());
        assertEquals(admin1.getCpf(), reloaded.get().getCpf());
        assertEquals(admin1.getNome(), reloaded.get().getNome());
    }

    @Test
    void testConvertMapToJsonAndParseJsonToMap() {
        adminRepository.save(admin1);

        String json = adminRepository.convertMapToJson();
        assertNotNull(json);
        assertTrue(json.contains(admin1.getNome()));
        assertTrue(json.contains(admin1.getEmail()));

        // Simula parsing manual
        var parsedMap = adminRepository.parseJsonToMap(json);
        assertNotNull(parsedMap);
        assertEquals(1, parsedMap.size());

        Admin parsedAdmin = parsedMap.get(admin1.getCpf());
        assertNotNull(parsedAdmin);
        assertEquals(admin1.getNome(), parsedAdmin.getNome());
    }

    @Test
    void testLoadFromFileShouldHandleEmptyFileGracefully() throws Exception {
        File file = new File(TEST_FILENAME);
        Files.writeString(file.toPath(), "");

        AdminRepository freshRepo = new AdminRepository();
        List<Admin> admins = freshRepo.findAll();
        assertNotNull(admins);
        assertTrue(admins.isEmpty());
    }

    @AfterEach
    void tearDown() {
        File file = new File(TEST_FILENAME);
        if (file.exists()) file.delete();
    }
}
