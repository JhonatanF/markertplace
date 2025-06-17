package com.marketplace.repository;

import com.marketplace.model.Loja;
import org.junit.jupiter.api.*;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class LojaRepositoryTest {

    private LojaRepository lojaRepository;
    private Loja loja;
    private final String filePath = "lojas-v1.json";

    @BeforeEach
    void setUp() {
        // Apaga o arquivo para garantir ambiente limpo de testes
        File file = new File(filePath);
        if (file.exists()) file.delete();

        lojaRepository = new LojaRepository();

        loja = new Loja("Loja Teste", "loja@teste.com", "senha123", "12345678901234", "Rua Teste, 123");
    }

    @AfterEach
    void cleanUp() {
        File file = new File(filePath);
        if (file.exists()) file.delete();
    }

    @Test
    void testSaveAndFindById() {
        lojaRepository.save(loja);

        Optional<Loja> foundLoja = lojaRepository.findById(loja.getCpfCnpj());

        assertTrue(foundLoja.isPresent());
        Loja l = foundLoja.get();

        assertEquals(loja.getNome(), l.getNome());
        assertEquals(loja.getEmail(), l.getEmail());
        assertEquals(loja.getSenha(), l.getSenha());
        assertEquals(loja.getCpfCnpj(), l.getCpfCnpj());
        assertEquals(loja.getEndereco(), l.getEndereco());
    }

    @Test
    void testFindAll() {
        lojaRepository.save(loja);

        List<Loja> lojas = lojaRepository.findAll();
        assertFalse(lojas.isEmpty());
        assertTrue(lojas.stream().anyMatch(l -> l.getCpfCnpj().equals(loja.getCpfCnpj())));
    }

    @Test
    void testDelete() {
        lojaRepository.save(loja);
        assertTrue(lojaRepository.exists(loja.getCpfCnpj()));

        lojaRepository.delete(loja.getCpfCnpj());
        assertFalse(lojaRepository.exists(loja.getCpfCnpj()));
    }

    @Test
    void testExists() {
        assertFalse(lojaRepository.exists(loja.getCpfCnpj()));

        lojaRepository.save(loja);
        assertTrue(lojaRepository.exists(loja.getCpfCnpj()));
    }

    @Test
    void testFindByNonExistentId() {
        Optional<Loja> notFound = lojaRepository.findById("00000000000000");
        assertFalse(notFound.isPresent());
    }

    @Test
    void testParseJsonToMap_ValidJson() {
        String json = "{\n" +
                "  \"12345678901234\": {\n" +
                "    \"nome\": \"Loja A\",\n" +
                "    \"email\": \"lojaa@example.com\",\n" +
                "    \"senha\": \"senhaA\",\n" +
                "    \"cpfCnpj\": \"12345678901234\",\n" +
                "    \"endereco\": \"Rua A, 1\"\n" +
                "  },\n" +
                "  \"98765432109876\": {\n" +
                "    \"nome\": \"Loja B\",\n" +
                "    \"email\": \"lojab@example.com\",\n" +
                "    \"senha\": \"senhaB\",\n" +
                "    \"cpfCnpj\": \"98765432109876\",\n" +
                "    \"endereco\": \"Rua B, 2\"\n" +
                "  }\n" +
                "}";

        var map = lojaRepository.parseJsonToMap(json);

        assertEquals(2, map.size());

        Loja lojaA = map.get("12345678901234");
        assertNotNull(lojaA);
        assertEquals("Loja A", lojaA.getNome());
        assertEquals("lojaa@example.com", lojaA.getEmail());

        Loja lojaB = map.get("98765432109876");
        assertNotNull(lojaB);
        assertEquals("Loja B", lojaB.getNome());
    }

    @Test
    void testParseJsonToMap_EmptyOrNullJson() {
        assertTrue(lojaRepository.parseJsonToMap(null).isEmpty());
        assertTrue(lojaRepository.parseJsonToMap("").isEmpty());
        assertTrue(lojaRepository.parseJsonToMap("  ").isEmpty());
    }

    @Test
    void testParseJsonToMap_InvalidJsonReturnsEmpty() {
        // JSON sem os campos obrigatórios
        String invalidJson = "{\"nome\":\"Loja X\"}";
        var map = lojaRepository.parseJsonToMap(invalidJson);
        assertTrue(map.isEmpty());
    }

    @Test
    void testConvertMapToJson_SerializesCorrectly() {
        lojaRepository.save(loja);

        String json = lojaRepository.convertMapToJson();

        assertTrue(json.contains(loja.getCpfCnpj()));
        assertTrue(json.contains(loja.getNome()));
        assertTrue(json.contains(loja.getEmail()));
        assertTrue(json.contains(loja.getSenha()));
        assertTrue(json.contains(loja.getEndereco()));

        // Test escaping characters
        Loja lojaEspecial = new Loja("Nome \"Especial\"", "email\nespecial@example.com", "senha\t123", "55555555555555", "Endereco\rEspecial");
        lojaRepository.save(lojaEspecial);
        String jsonEspecial = lojaRepository.convertMapToJson();
        assertTrue(jsonEspecial.contains("Nome \\\"Especial\\\""));
        assertTrue(jsonEspecial.contains("email\\nespecial@example.com"));
        assertTrue(jsonEspecial.contains("senha\\t123"));
        assertTrue(jsonEspecial.contains("Endereco\\rEspecial"));
    }

    @Test
    void testSaveMultipleAndFindAll() {
        Loja l2 = new Loja("Loja 2", "loja2@teste.com", "senha2", "22222222222222", "Rua 2, 456");
        lojaRepository.save(loja);
        lojaRepository.save(l2);

        List<Loja> lojas = lojaRepository.findAll();
        assertEquals(2, lojas.size());

        assertTrue(lojas.stream().anyMatch(l -> l.getCpfCnpj().equals(loja.getCpfCnpj())));
        assertTrue(lojas.stream().anyMatch(l -> l.getCpfCnpj().equals(l2.getCpfCnpj())));
    }
}
