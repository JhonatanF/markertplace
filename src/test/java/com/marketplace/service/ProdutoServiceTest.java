package com.marketplace.service;

import com.marketplace.model.Loja;
import com.marketplace.model.Produto;
import com.marketplace.repository.LojaRepository;
import com.marketplace.repository.ProdutoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.nio.file.Path;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class ProdutoServiceTest {
    @TempDir
    Path tempDir;
    
    private ProdutoRepository produtoRepository;
    private LojaRepository lojaRepository;
    private ProdutoService service;
    private Produto produto;
    private Loja loja;

    @BeforeEach
    void setUp() {
        produtoRepository = new ProdutoRepository();
        lojaRepository = new LojaRepository();
        service = new ProdutoService(produtoRepository, lojaRepository);
        
        loja = new Loja("Loja Teste", "loja@teste.com", "senha123", "12345678901", "Rua Teste, 123");
        lojaRepository.save(loja);
        
        produto = new Produto("Produto Teste", 99.99, "Tipo", 10, "Marca", "Descrição", loja.getCpfCnpj());
    }

    @Test
    void testCadastrarProduto() {
        Produto cadastrado = service.cadastrar(produto);
        
        assertNotNull(cadastrado.getId());
        assertEquals(produto.getNome(), cadastrado.getNome());
        assertEquals(produto.getValor(), cadastrado.getValor());
        assertEquals(produto.getTipo(), cadastrado.getTipo());
        assertEquals(produto.getQuantidade(), cadastrado.getQuantidade());
        assertEquals(produto.getMarca(), cadastrado.getMarca());
        assertEquals(produto.getDescricao(), cadastrado.getDescricao());
        assertEquals(produto.getLojaCpfCnpj(), cadastrado.getLojaCpfCnpj());
    }

    @Test
    void testCadastrarProdutoComLojaInexistente() {
        produto.setLojaCpfCnpj("loja-inexistente");
        assertThrows(IllegalArgumentException.class, () ->
            service.cadastrar(produto)
        );
    }

    @Test
    void testCadastrarProdutoComQuantidadeNegativa() {
        produto.setQuantidade(-1);
        assertThrows(IllegalArgumentException.class, () ->
            service.cadastrar(produto)
        );
    }

    @Test
    void testCadastrarProdutoComValorNegativo() {
        produto.setValor(-1.0);
        assertThrows(IllegalArgumentException.class, () ->
            service.cadastrar(produto)
        );
    }

    @Test
    void testBuscarProduto() {
        Produto cadastrado = service.cadastrar(produto);
        
        Optional<Produto> encontrado = service.buscarPorId(cadastrado.getId());
        assertTrue(encontrado.isPresent());
        assertEquals(cadastrado.getId(), encontrado.get().getId());
    }

    @Test
    void testListarProdutos() {
        service.cadastrar(produto);
        
        Produto produto2 = new Produto("Outro Produto", 50.0, "Tipo2", 5, "Marca2", 
                                     "Desc2", produto.getLojaCpfCnpj());
        service.cadastrar(produto2);

        List<Produto> produtos = service.listarTodos();
        assertEquals(2, produtos.size());
    }

    @Test
    void testListarProdutosPorLoja() {
        service.cadastrar(produto);
        
        Produto produto2 = new Produto("Outro Produto", 50.0, "Tipo2", 5, "Marca2", 
                                     "Desc2", produto.getLojaCpfCnpj());
        service.cadastrar(produto2);

        List<Produto> produtos = service.listarPorLoja(produto.getLojaCpfCnpj());
        assertEquals(2, produtos.size());
        assertTrue(produtos.stream().allMatch(p -> p.getLojaCpfCnpj().equals(produto.getLojaCpfCnpj())));
    }

    @Test
    void testAtualizarProduto() {
        Produto cadastrado = service.cadastrar(produto);
        
        cadastrado.setNome("Nome Atualizado");
        cadastrado.setValor(199.99);
        cadastrado.setTipo("Novo Tipo");
        cadastrado.setQuantidade(20);
        cadastrado.setMarca("Nova Marca");
        cadastrado.setDescricao("Nova Descrição");

        Produto atualizado = service.atualizar(cadastrado);
        
        assertEquals("Nome Atualizado", atualizado.getNome());
        assertEquals(199.99, atualizado.getValor());
        assertEquals("Novo Tipo", atualizado.getTipo());
        assertEquals(20, atualizado.getQuantidade());
        assertEquals("Nova Marca", atualizado.getMarca());
        assertEquals("Nova Descrição", atualizado.getDescricao());
    }

    @Test
    void testAtualizarProdutoInexistente() {
        produto.setId("id-inexistente");
        assertThrows(IllegalArgumentException.class, () ->
            service.atualizar(produto)
        );
    }

    @Test
    void testRemoverProduto() {
        Produto cadastrado = service.cadastrar(produto);
        
        assertTrue(service.buscarPorId(cadastrado.getId()).isPresent());
        service.remover(cadastrado.getId());
        assertFalse(service.buscarPorId(cadastrado.getId()).isPresent());
    }
}
