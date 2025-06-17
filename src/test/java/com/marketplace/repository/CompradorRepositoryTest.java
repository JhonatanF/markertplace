package com.marketplace.repository;

import com.marketplace.model.Comprador;
import org.junit.jupiter.api.*;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class CompradorRepositoryTest {

    private CompradorRepository compradorRepository;
    private Comprador comprador;
    private final String testFilePath = "compradores-v1.json";

    @BeforeEach
    void setUp() {
        // Limpa o arquivo antes de cada teste
        File file = new File(testFilePath);
        if (file.exists()) file.delete();

        compradorRepository = new CompradorRepository();
        comprador = new Comprador("Comprador Teste", "comprador@teste.com", "senha123", "12345678901", "Avenida Teste, 456");
        comprador.setPontuacao(50);
    }

    @Test
    void testSaveAndFindById() {
        compradorRepository.save(comprador);
        Comprador found = compradorRepository.findById(comprador.getCpf()).orElse(null);

        assertNotNull(found);
        assertEquals("Comprador Teste", found.getNome());
        assertEquals("comprador@teste.com", found.getEmail());
        assertEquals("senha123", found.getSenha());
        assertEquals("12345678901", found.getCpf());
        assertEquals("Avenida Teste, 456", found.getEndereco());
        assertEquals(50, found.getPontuacao());
    }

    @Test
    void testFindAll() {
        compradorRepository.save(comprador);
        List<Comprador> compradores = compradorRepository.findAll();

        assertFalse(compradores.isEmpty());
        assertTrue(compradores.stream().anyMatch(c -> c.getCpf().equals(comprador.getCpf())));
    }

    @Test
    void testDelete() {
        compradorRepository.save(comprador);
        assertTrue(compradorRepository.exists(comprador.getCpf()));

        compradorRepository.delete(comprador.getCpf());
        assertFalse(compradorRepository.exists(comprador.getCpf()));
    }

    @Test
    void testExists() {
        assertFalse(compradorRepository.exists(comprador.getCpf()));
        compradorRepository.save(comprador);
        assertTrue(compradorRepository.exists(comprador.getCpf()));
    }

    @Test
    void testFindByNonExistentId() {
        Optional<Comprador> notFound = compradorRepository.findById("00000000000");
        assertFalse(notFound.isPresent());
    }

    @Test
    void testPersistenciaERecarregamento() {
        compradorRepository.save(comprador);

        // Reinstancia repositório para testar carregamento do arquivo
        CompradorRepository novoRepo = new CompradorRepository();
        Optional<Comprador> loaded = novoRepo.findById(comprador.getCpf());

        assertTrue(loaded.isPresent());
        assertEquals(comprador.getPontuacao(), loaded.get().getPontuacao());
    }

    @Test
    void testPontuacaoPadrao() {
        Comprador novo = new Comprador("Novo", "novo@teste.com", "123", "11122233344", "Rua Nova");
        compradorRepository.save(novo);

        Optional<Comprador> resultado = compradorRepository.findById("11122233344");
        assertTrue(resultado.isPresent());
        assertEquals(0, resultado.get().getPontuacao(), "Pontuação padrão deve ser 0");
    }

    @Test
    void testArquivoVazioNaoQuebra() throws IOException {
        try (FileWriter writer = new FileWriter(testFilePath)) {
            writer.write("");
        }

        assertDoesNotThrow(() -> new CompradorRepository(), "Repositório deve lidar com arquivo vazio");
    }

    @Test
    void testConversaoParaJson() {
        compradorRepository.save(comprador);
        String json = compradorRepository.convertMapToJson();

        assertTrue(json.contains("\"cpf\": \"12345678901\""));
        assertTrue(json.contains("\"pontuacao\": 50"));
        assertTrue(json.contains("\"nome\": \"Comprador Teste\""));
    }

    @AfterEach
    void cleanUp() {
        File file = new File(testFilePath);
        if (file.exists()) file.delete();
    }
}
