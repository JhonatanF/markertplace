package com.marketplace.repository;

import com.marketplace.model.Produto;
import org.junit.jupiter.api.*;

import java.io.File;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ProdutoRepositoryTest {

    private ProdutoRepository produtoRepository;
    private Produto produto;
    private final String filePath = "produtos-v1.json";

    @BeforeEach
    void setUp() {
        // Limpa arquivo para evitar interferência nos testes
        File file = new File(filePath);
        if (file.exists()) file.delete();

        produtoRepository = new ProdutoRepository();

        produto = new Produto("produto1", "Produto Teste", 100.0, "Categoria A", 10, "Marca X", "Descrição do Produto", "00000000000191");
    }

    @AfterEach
    void cleanUp() {
        File file = new File(filePath);
        if (file.exists()) file.delete();
    }

    @Test
    void testSaveAndFindById() {
        produtoRepository.save(produto);

        Optional<Produto> found = produtoRepository.findById(produto.getId());
        assertTrue(found.isPresent());

        Produto p = found.get();
        assertEquals(produto.getNome(), p.getNome());
        assertEquals(produto.getValor(), p.getValor());
        assertEquals(produto.getTipo(), p.getTipo());
        assertEquals(produto.getQuantidade(), p.getQuantidade());
        assertEquals(produto.getMarca(), p.getMarca());
        assertEquals(produto.getDescricao(), p.getDescricao());
        assertEquals(produto.getLojaCpfCnpj(), p.getLojaCpfCnpj());
    }

    @Test
    void testFindAll() {
        produtoRepository.save(produto);

        List<Produto> produtos = produtoRepository.findAll();
        assertFalse(produtos.isEmpty());
        assertTrue(produtos.stream().anyMatch(p -> p.getId().equals(produto.getId())));
    }

    @Test
    void testDelete() {
        produtoRepository.save(produto);
        assertTrue(produtoRepository.exists(produto.getId()));

        produtoRepository.delete(produto.getId());
        assertFalse(produtoRepository.exists(produto.getId()));
    }

    @Test
    void testExists() {
        assertFalse(produtoRepository.exists(produto.getId()));

        produtoRepository.save(produto);
        assertTrue(produtoRepository.exists(produto.getId()));
    }

    @Test
    void testFindByNonExistentId() {
        Optional<Produto> notFound = produtoRepository.findById("id-inexistente");
        assertFalse(notFound.isPresent());
    }

    @Test
    void testFindByLoja() {
        produtoRepository.save(produto);

        List<Produto> produtosLoja = produtoRepository.findByLoja(produto.getLojaCpfCnpj());
        assertFalse(produtosLoja.isEmpty());
        assertTrue(produtosLoja.stream().allMatch(p -> p.getLojaCpfCnpj().equals(produto.getLojaCpfCnpj())));
    }

    @Test
    void testParseJsonToMap_ValidJson() {
        String json = "{\n" +
                "  \"prod1\": {\n" +
                "    \"id\": \"prod1\",\n" +
                "    \"nome\": \"Produto 1\",\n" +
                "    \"valor\": 50.5,\n" +
                "    \"tipo\": \"Tipo1\",\n" +
                "    \"quantidade\": 5,\n" +
                "    \"marca\": \"Marca1\",\n" +
                "    \"descricao\": \"Descrição 1\",\n" +
                "    \"lojaCpfCnpj\": \"11111111111111\"\n" +
                "  },\n" +
                "  \"prod2\": {\n" +
                "    \"id\": \"prod2\",\n" +
                "    \"nome\": \"Produto 2\",\n" +
                "    \"valor\": 75.0,\n" +
                "    \"tipo\": \"Tipo2\",\n" +
                "    \"quantidade\": 3,\n" +
                "    \"marca\": \"Marca2\",\n" +
                "    \"descricao\": \"Descrição 2\",\n" +
                "    \"lojaCpfCnpj\": \"22222222222222\"\n" +
                "  }\n" +
                "}";

        var map = produtoRepository.parseJsonToMap(json);

        assertEquals(2, map.size());

        Produto prod1 = map.get("prod1");
        assertNotNull(prod1);
        assertEquals("Produto 1", prod1.getNome());
        assertEquals(50.5, prod1.getValor());
        assertEquals(5, prod1.getQuantidade());

        Produto prod2 = map.get("prod2");
        assertNotNull(prod2);
        assertEquals("Produto 2", prod2.getNome());
    }

    @Test
    void testParseJsonToMap_EmptyOrNullJson() {
        assertTrue(produtoRepository.parseJsonToMap(null).isEmpty());
        assertTrue(produtoRepository.parseJsonToMap("").isEmpty());
        assertTrue(produtoRepository.parseJsonToMap("  ").isEmpty());
    }

    @Test
    void testParseJsonToMap_InvalidJsonReturnsEmpty() {
        // JSON sem todos os campos obrigatórios
        String invalidJson = "{\"id\":\"prod3\",\"nome\":\"Produto 3\"}";
        var map = produtoRepository.parseJsonToMap(invalidJson);
        assertTrue(map.isEmpty());
    }

    @Test
    void testConvertMapToJson_SerializesCorrectly() {
        produtoRepository.save(produto);

        String json = produtoRepository.convertMapToJson();

        assertTrue(json.contains(produto.getId()));
        assertTrue(json.contains(produto.getNome()));
        assertTrue(json.contains(String.valueOf(produto.getValor())));
        assertTrue(json.contains(produto.getTipo()));
        assertTrue(json.contains(String.valueOf(produto.getQuantidade())));
        assertTrue(json.contains(produto.getMarca()));
        assertTrue(json.contains(produto.getDescricao()));
        assertTrue(json.contains(produto.getLojaCpfCnpj()));

        // Teste de escape de caracteres especiais
        Produto pEspecial = new Produto(
                "id2",
                "Nome \"Especial\"",
                123.45,
                "Tipo\nEspecial",
                20,
                "Marca\tEspecial",
                "Descrição\rEspecial",
                "33333333333333"
        );
        produtoRepository.save(pEspecial);
        String jsonEspecial = produtoRepository.convertMapToJson();

        assertTrue(jsonEspecial.contains("Nome \\\"Especial\\\""));
        assertTrue(jsonEspecial.contains("Tipo\\nEspecial"));
        assertTrue(jsonEspecial.contains("Marca\\tEspecial"));
        assertTrue(jsonEspecial.contains("Descrição\\rEspecial"));
    }

    @Test
    void testSaveMultipleAndFindAll() {
        Produto p2 = new Produto("prod2", "Produto 2", 200.0, "Categoria B", 5, "Marca Y", "Outro produto", "99999999999999");
        produtoRepository.save(produto);
        produtoRepository.save(p2);

        List<Produto> produtos = produtoRepository.findAll();
        assertEquals(2, produtos.size());
        assertTrue(produtos.stream().anyMatch(p -> p.getId().equals(produto.getId())));
        assertTrue(produtos.stream().anyMatch(p -> p.getId().equals(p2.getId())));
    }
}
