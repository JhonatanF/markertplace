package com.marketplace.facade;

import com.marketplace.model.Comprador;
import com.marketplace.model.Loja;
import com.marketplace.model.Produto;
import com.marketplace.repository.AdminRepository;
import com.marketplace.repository.CompradorRepository;
import com.marketplace.repository.LojaRepository;
import com.marketplace.repository.ProdutoRepository;
import com.marketplace.service.AdminService;
import com.marketplace.service.CompradorService;
import com.marketplace.service.LojaService;
import com.marketplace.service.ProdutoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.nio.file.Path;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class MarketplaceFacadeTest {
    @TempDir
    Path tempDir;
    
    private MarketplaceFacade facade;
    private Loja loja;
    private Comprador comprador;
    private Produto produto;

    @BeforeEach
    void setUp() {
        LojaRepository lojaRepo = new LojaRepository();
        CompradorRepository compradorRepo = new CompradorRepository();
        ProdutoRepository produtoRepo = new ProdutoRepository();
        AdminRepository adminRepo = new AdminRepository();

        LojaService lojaService = new LojaService(lojaRepo);
        CompradorService compradorService = new CompradorService(compradorRepo);
        ProdutoService produtoService = new ProdutoService(produtoRepo, lojaRepo);
        AdminService adminService = new AdminService(adminRepo);

        facade = new MarketplaceFacade(lojaService, compradorService, produtoService, adminService);

        // Criar dados de teste
        loja = new Loja("Loja Teste", "loja@teste.com", "senha123", "12345678901", "Rua Teste, 123");
        comprador = new Comprador("Comprador Teste", "comprador@teste.com", "senha123", "98765432101", "Rua Comprador, 456");
        produto = new Produto("Produto Teste", 99.99, "Tipo", 10, "Marca", "Descrição", loja.getCpfCnpj());
    }

    @Test
    void testFluxoCompletoLoja() {
        // Cadastrar
        Loja cadastrada = facade.cadastrarLoja(loja.getNome(), loja.getEmail(), loja.getSenha(), 
                                             loja.getCpfCnpj(), loja.getEndereco());
        assertNotNull(cadastrada);
        
        // Buscar
        Optional<Loja> encontrada = facade.buscarLoja(loja.getCpfCnpj());
        assertTrue(encontrada.isPresent());
        
        // Listar
        List<Loja> lojas = facade.listarLojas();
        assertEquals(1, lojas.size());
        
        // Atualizar
        String novoNome = "Loja Atualizada";
        Loja atualizada = facade.atualizarLoja(novoNome, loja.getEmail(), loja.getSenha(), 
                                              loja.getCpfCnpj(), loja.getEndereco());
        assertEquals(novoNome, atualizada.getNome());
        
        // Remover
        facade.removerLoja(loja.getCpfCnpj());
        assertFalse(facade.buscarLoja(loja.getCpfCnpj()).isPresent());
    }

    @Test
    void testFluxoCompletoComprador() {
        // Cadastrar
        Comprador cadastrado = facade.cadastrarComprador(comprador.getNome(), comprador.getEmail(), 
                                                       comprador.getSenha(), comprador.getCpf(), 
                                                       comprador.getEndereco());
        assertNotNull(cadastrado);
        
        // Buscar
        Optional<Comprador> encontrado = facade.buscarComprador(comprador.getCpf());
        assertTrue(encontrado.isPresent());
        
        // Listar
        List<Comprador> compradores = facade.listarCompradores();
        assertEquals(1, compradores.size());
        
        // Atualizar
        String novoNome = "Comprador Atualizado";
        Comprador atualizado = facade.atualizarComprador(novoNome, comprador.getEmail(), 
                                                       comprador.getSenha(), comprador.getCpf(), 
                                                       comprador.getEndereco());
        assertEquals(novoNome, atualizado.getNome());
        
        // Remover
        facade.removerComprador(comprador.getCpf());
        assertFalse(facade.buscarComprador(comprador.getCpf()).isPresent());
    }

    @Test
    void testFluxoCompletoProduto() {
        // Primeiro cadastrar a loja
        facade.cadastrarLoja(loja.getNome(), loja.getEmail(), loja.getSenha(), 
                           loja.getCpfCnpj(), loja.getEndereco());
        
        // Cadastrar produto
        Produto cadastrado = facade.cadastrarProduto(produto.getNome(), produto.getValor(), 
                                                   produto.getTipo(), produto.getQuantidade(), 
                                                   produto.getMarca(), produto.getDescricao(), 
                                                   produto.getLojaCpfCnpj());
        assertNotNull(cadastrado);
        assertNotNull(cadastrado.getId());
        
        // Buscar
        Optional<Produto> encontrado = facade.buscarProduto(cadastrado.getId());
        assertTrue(encontrado.isPresent());
        
        // Listar
        List<Produto> produtos = facade.listarProdutos();
        assertEquals(1, produtos.size());
        
        // Listar por loja
        List<Produto> produtosDaLoja = facade.listarProdutosDaLoja(loja.getCpfCnpj());
        assertEquals(1, produtosDaLoja.size());
        
        // Atualizar
        String novoNome = "Produto Atualizado";
        Produto atualizado = facade.atualizarProduto(cadastrado.getId(), novoNome, produto.getValor(), 
                                                    produto.getTipo(), produto.getQuantidade(), 
                                                    produto.getMarca(), produto.getDescricao(), 
                                                    produto.getLojaCpfCnpj());
        assertEquals(novoNome, atualizado.getNome());
        
        // Remover
        facade.removerProduto(cadastrado.getId());
        assertFalse(facade.buscarProduto(cadastrado.getId()).isPresent());
    }

    @Test
    void testCadastrarProdutoComLojaInexistente() {
        assertThrows(IllegalArgumentException.class, () ->
            facade.cadastrarProduto(produto.getNome(), produto.getValor(), produto.getTipo(),
                                  produto.getQuantidade(), produto.getMarca(), produto.getDescricao(),
                                  "loja-inexistente")
        );
    }
}
