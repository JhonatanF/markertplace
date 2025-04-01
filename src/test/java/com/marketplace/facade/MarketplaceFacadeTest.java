package com.marketplace.facade;

import com.marketplace.model.Admin;
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

class MarketplaceFacadeTest{
    @TempDir
    Path tempDir;
    
    private MarketplaceFacade facade;
    private Admin admin;
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
        admin = new Admin("Admin Teste", "admin@teste.com", "senha123", "12345678901", "Rua Admin, 123");
        loja = new Loja("Loja Teste", "loja@teste.com", "senha123", "12345678901", "Rua Teste, 123");
        comprador = new Comprador("Comprador Teste", "comprador@teste.com", "senha123", "98765432101", "Rua Comprador, 456");
        produto = new Produto("Produto Teste", 99.99, "Tipo", 10, "Marca", "Descrição", loja.getCpfCnpj());

        facade.removerAdmins();
        facade.removerLojas();
        facade.removerProdutos();
        facade.removerCompradores();
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
        
        // Remover
        facade.removerProduto(cadastrado.getId());
        assertFalse(facade.buscarProduto(cadastrado.getId()).isPresent());
    }

    @Test
    void testFluxoCompletoAdmin() {
        // Cadastrar um Admin
        Admin adminCadastrado = facade.cadastrarAdmin(admin.getNome(), admin.getEmail(), admin.getSenha(), admin.getCpf(), admin.getEndereco());

        assertNotNull(adminCadastrado);
        assertEquals("Admin Teste", adminCadastrado.getNome());
        assertEquals("admin@teste.com", adminCadastrado.getEmail());
        assertEquals("12345678901", adminCadastrado.getCpf());

        // Buscar o Admin cadastrado
        Optional<Admin> adminBuscado = facade.buscarAdmin(adminCadastrado.getCpf());
        assertTrue(adminBuscado.isPresent(), "Admin deve ser encontrado.");
        assertEquals(adminCadastrado.getCpf(), adminBuscado.get().getCpf(), "CPF deve ser o mesmo.");

        // Listar todos os Admins (deve retornar o admin cadastrado)
        List<Admin> admins = facade.listarAdmins();
        assertEquals(1, admins.size(), "Deve existir apenas um admin cadastrado.");

        // Atualizar dados do Admin
        String novoNome = "Admin Atualizado";
        Admin adminAtualizado = facade.atualizarAdmin(novoNome, adminCadastrado.getEmail(), adminCadastrado.getSenha(), adminCadastrado.getCpf(), adminCadastrado.getEndereco());
        assertNotNull(adminAtualizado);
        assertEquals(novoNome, adminAtualizado.getNome(), "Nome do Admin deve ser atualizado.");

        // Remover o Admin
        facade.removerAdmin(adminCadastrado.getCpf());
        assertFalse(facade.buscarAdmin(adminCadastrado.getCpf()).isPresent(), "Admin deve ser removido com sucesso.");
    }


    @Test
    void testCadastrarProdutoComLojaInexistente() {
        assertThrows(IllegalArgumentException.class, () ->
            facade.cadastrarProduto(produto.getNome(), produto.getValor(), produto.getTipo(),
                                  produto.getQuantidade(), produto.getMarca(), produto.getDescricao(),
                                  "loja-inexistente")
        );
    }

    @Test
    void testCadastrarLojaComCpfCnpjExistente() {
        // Cadastrar uma loja
        facade.cadastrarLoja(loja.getNome(), loja.getEmail(), loja.getSenha(), loja.getCpfCnpj(), loja.getEndereco());

        // Tentar cadastrar uma nova loja com o mesmo CPF/CNPJ
        assertThrows(IllegalArgumentException.class, () ->
                facade.cadastrarLoja("Outra Loja", "outro@teste.com", "senha123", loja.getCpfCnpj(), "Rua Teste, 456")
        );
    }

    @Test
    void testCadastrarCompradorComCpfExistente() {
        // Cadastrar um comprador
        facade.cadastrarComprador(comprador.getNome(), comprador.getEmail(), comprador.getSenha(), comprador.getCpf(), comprador.getEndereco());

        // Tentar cadastrar um novo comprador com o mesmo CPF
        assertThrows(IllegalArgumentException.class, () ->
                facade.cadastrarComprador("Outro Comprador", "outro@teste.com", "senha123", comprador.getCpf(), "Rua Comprador, 789")
        );
    }

    @Test
    void testAtualizarLojaInexistente() {
        // Tentando atualizar uma loja que não foi cadastrada
        assertThrows(IllegalArgumentException.class, () ->
                facade.atualizarLoja("Loja Atualizada", "loja@teste.com", "senha123", "99999999999", "Rua Teste, 123")
        );
    }

    @Test
    void testRemoverCompradorInexistente() {
        // Tentando remover um comprador que não foi cadastrado
        assertThrows(IllegalArgumentException.class, () ->
                facade.removerComprador("11122233344")
        );
    }

    @Test
    void testRemoverProdutoInexistente() {
        // Tentando remover um produto que não existe
        assertThrows(IllegalArgumentException.class, () ->
                facade.removerProduto("produto-inexistente-id")
        );
    }

    @Test
    void testBuscarProdutoInexistente() {
        // Buscando um produto inexistente
        Optional<Produto> produtoInexistente = facade.buscarProduto("produto-inexistente-id");
        assertFalse(produtoInexistente.isPresent(), "Produto não deveria ser encontrado.");
    }

    @Test
    void testListarProdutosDaLojaVazia() {
        // Garantir que a loja não tenha produtos cadastrados
        List<Produto> produtosDaLoja = facade.listarProdutosDaLoja(loja.getCpfCnpj());
        assertTrue(produtosDaLoja.isEmpty(), "A lista de produtos da loja deve estar vazia.");
    }

    @Test
    void testBuscarLojaInexistente() {
        // Tentando buscar uma loja inexistente
        Optional<Loja> lojaInexistente = facade.buscarLoja("99999999999");
        assertFalse(lojaInexistente.isPresent(), "Loja não deveria ser encontrada.");
    }

    @Test
    void testListarAdminsVazio() {
        // Garantir que não há admins cadastrados
        List<Admin> admins = facade.listarAdmins();
        assertTrue(admins.isEmpty(), "A lista de administradores deve estar vazia.");
    }

    @Test
    void testCadastrarAdminComCpfExistente() {
        // Cadastrar um admin
        facade.cadastrarAdmin("Admin Teste", "admin@teste.com", "senha123", "12345678901", "Rua Admin, 123");

        // Tentar cadastrar um novo admin com o mesmo CPF
        assertThrows(IllegalArgumentException.class, () ->
                facade.cadastrarAdmin("Outro Admin", "outro@teste.com", "senha123", "12345678901", "Rua Admin, 456")
        );
    }
}
