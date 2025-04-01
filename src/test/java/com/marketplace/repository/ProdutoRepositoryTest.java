package com.marketplace.repository;

import com.marketplace.model.Produto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import java.util.List;
import java.util.Optional;

public class ProdutoRepositoryTest {

    private ProdutoRepository produtoRepository;
    private Produto produto;

    @BeforeEach
    void setUp() {
        // Inicializa o repositório
        produtoRepository = new ProdutoRepository();

        // Cria um novo produto usando o construtor com os novos parâmetros
        produto = new Produto("Produto Teste", 100.0, "Categoria A", 10, "Marca X", "Descrição do Produto", "00000000000191");
    }

    @Test
    void testSaveAndFindById() {
        // Testa salvar um produto e encontrá-lo pelo ID
        produtoRepository.save(produto);

        Produto foundProduto = produtoRepository.findById(produto.getId()).orElse(null);

        assertNotNull(foundProduto, "Produto deveria ser encontrado.");
        assertEquals(produto.getNome(), foundProduto.getNome(), "Os nomes dos produtos devem ser iguais.");
        assertEquals(produto.getValor(), foundProduto.getValor(), "Os valores dos produtos devem ser iguais.");
        assertEquals(produto.getTipo(), foundProduto.getTipo(), "Os tipos dos produtos devem ser iguais.");
        assertEquals(produto.getQuantidade(), foundProduto.getQuantidade(), "As quantidades dos produtos devem ser iguais.");
        assertEquals(produto.getMarca(), foundProduto.getMarca(), "As marcas dos produtos devem ser iguais.");
        assertEquals(produto.getDescricao(), foundProduto.getDescricao(), "As descrições dos produtos devem ser iguais.");
        assertEquals(produto.getLojaCpfCnpj(), foundProduto.getLojaCpfCnpj(), "Os CNPJs das lojas devem ser iguais.");
    }

    @Test
    void testFindAll() {
        // Testa a recuperação de todos os produtos
        produtoRepository.save(produto);

        List<Produto> produtos = produtoRepository.findAll();
        assertFalse(produtos.isEmpty(), "A lista de produtos não deve estar vazia.");
        assertTrue(produtos.contains(produto), "Produto deveria estar na lista.");
    }

    @Test
    void testDelete() {
        // Testa a exclusão de um produto
        produtoRepository.save(produto);
        assertTrue(produtoRepository.exists(produto.getId()), "Produto deveria existir antes de ser excluído.");

        produtoRepository.delete(produto.getId());
        assertFalse(produtoRepository.exists(produto.getId()), "Produto não deveria existir após exclusão.");
    }

    @Test
    void testExists() {
        // Testa a verificação de existência de um produto
        assertFalse(produtoRepository.exists(produto.getId()), "Produto não deveria existir antes de ser salvo.");

        produtoRepository.save(produto);
        assertTrue(produtoRepository.exists(produto.getId()), "Produto deveria existir após ser salvo.");
    }

    @Test
    void testFindByLoja() {
        // Testa a busca de produtos por loja (CNPJ)
        produtoRepository.save(produto);

        List<Produto> produtosLoja = produtoRepository.findByLoja(produto.getLojaCpfCnpj());
        assertFalse(produtosLoja.isEmpty(), "Deveria haver produtos da loja com o CNPJ especificado.");
        assertTrue(produtosLoja.stream().anyMatch(p -> p.getLojaCpfCnpj().equals(produto.getLojaCpfCnpj())),
                "Produto da loja deveria estar presente na lista.");
    }

    @Test
    void testFindByNonExistentId() {
        // Testa a busca por um ID inexistente
        Optional<Produto> foundProduto = produtoRepository.findById("id-inexistente");
        assertFalse(foundProduto.isPresent(), "Produto com ID inexistente não deveria ser encontrado.");
    }
}
