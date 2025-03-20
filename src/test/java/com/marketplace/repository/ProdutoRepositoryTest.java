package com.marketplace.repository;

import com.marketplace.model.Produto;
import com.marketplace.utils.TestDataLoader;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class ProdutoRepositoryTest {
    private ProdutoRepository repository;
    private Produto produto;

    @BeforeEach
    void setUp() {
        repository = new ProdutoRepository();
        produto = TestDataLoader.loadProdutos().get(0);
    }



    @Test
    void testSaveAndFind() {
        repository.save(produto);
        Optional<Produto> found = repository.findById(produto.getId());
        
        assertTrue(found.isPresent());
        assertEquals(produto.getNome(), found.get().getNome());
        assertEquals(produto.getValor(), found.get().getValor());
        assertEquals(produto.getTipo(), found.get().getTipo());
        assertEquals(produto.getQuantidade(), found.get().getQuantidade());
        assertEquals(produto.getMarca(), found.get().getMarca());
        assertEquals(produto.getDescricao(), found.get().getDescricao());
        assertEquals(produto.getLojaCpfCnpj(), found.get().getLojaCpfCnpj());
    }

    @Test
    void testFindNonExistent() {
        Optional<Produto> found = repository.findById("id-inexistente");
        assertFalse(found.isPresent());
    }

    @Test
    void testFindAll() {
        List<Produto> produtos = TestDataLoader.loadProdutos();
        produtos.forEach(p -> repository.save(p));

        List<Produto> foundProdutos = repository.findAll();
        assertEquals(produtos.size(), foundProdutos.size());
        produtos.forEach(p -> 
            assertTrue(foundProdutos.stream()
                .anyMatch(fp -> fp.getId().equals(p.getId()))));
    }

    @Test
    void testFindAllEmpty() {
        List<Produto> produtos = repository.findAll();
        assertTrue(produtos.isEmpty());
    }

    @Test
    void testDelete() {
        repository.save(produto);
        assertTrue(repository.findById(produto.getId()).isPresent());
        
        repository.delete(produto.getId());
        assertFalse(repository.findById(produto.getId()).isPresent());
    }

    @Test
    void testDeleteNonExistent() {
        repository.delete("id-inexistente"); // Não deve lançar exceção
    }

    @Test
    void testUpdate() {
        repository.save(produto);
        
        produto.setNome("Nome Atualizado");
        produto.setValor(199.99);
        repository.save(produto);
        
        Optional<Produto> updated = repository.findById(produto.getId());
        assertTrue(updated.isPresent());
        assertEquals("Nome Atualizado", updated.get().getNome());
        assertEquals(199.99, updated.get().getValor());
    }

    @Test
    void testFindByLoja() {
        List<Produto> produtos = TestDataLoader.loadProdutos();
        String storeCpfCnpj = produto.getLojaCpfCnpj();
        produtos.forEach(p -> repository.save(p));

        List<Produto> storeProducts = repository.findByLoja(storeCpfCnpj);
        List<Produto> expectedProducts = produtos.stream()
            .filter(p -> p.getLojaCpfCnpj().equals(storeCpfCnpj))
            .toList();

        assertEquals(expectedProducts.size(), storeProducts.size());
        assertTrue(storeProducts.stream().allMatch(p -> p.getLojaCpfCnpj().equals(storeCpfCnpj)));
    }

    @Test
    void testExists() {
        assertFalse(repository.exists(produto.getId()));
        repository.save(produto);
        assertTrue(repository.exists(produto.getId()));
    }
}
