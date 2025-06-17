package com.marketplace.facade;

import com.marketplace.model.*;
import com.marketplace.repository.*;
import com.marketplace.service.*;
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
    private Avaliacao avaliacao;

    @BeforeEach
    void setUp() {
        LojaRepository lojaRepo = new LojaRepository();
        CompradorRepository compradorRepo = new CompradorRepository();
        ProdutoRepository produtoRepo = new ProdutoRepository();
        AdminRepository adminRepo = new AdminRepository();
        HistoricoRepository historicoRepo = new HistoricoRepository(Historico.class);
        AvaliacaoRepository avaliacaoRepo = new AvaliacaoRepository();

        LojaService lojaService = new LojaService(lojaRepo);
        CompradorService compradorService = new CompradorService(compradorRepo);
        ProdutoService produtoService = new ProdutoService(produtoRepo, lojaRepo);
        AdminService adminService = new AdminService(adminRepo);
        HistoricoService historicoService = new HistoricoService(historicoRepo);
        AvaliacaoService avaliacaoService = new AvaliacaoService(avaliacaoRepo);

        facade = new MarketplaceFacade(lojaService, compradorService, produtoService, adminService, historicoService, avaliacaoService);

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

    // @Test
    // void testListarProdutosDaLojaVazia() {
    //     // Garantir que a loja não tenha produtos cadastrados
    //     List<Produto> produtosDaLoja = facade.listarProdutosDaLoja(loja.getCpfCnpj());
    //     assertTrue(produtosDaLoja.isEmpty(), "A lista de produtos da loja deve estar vazia.");
    // }

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

    @Test
    void testBuscarLojaPorNome() {
        facade.cadastrarLoja("Loja Legal", "loja@legal.com", "senha", "11111111111", "Rua Legal, 1");

        List<Loja> resultado = facade.buscarLojaNome("Loja Legal");
        assertEquals(1, resultado.size());
        assertEquals("Loja Legal", resultado.get(0).getNome());
    }

    @Test
    void testBuscarProdutoPorNome() {
        facade.cadastrarLoja(loja.getNome(), loja.getEmail(), loja.getSenha(), loja.getCpfCnpj(), loja.getEndereco());
        facade.cadastrarProduto("Notebook", 3000.0, "Eletrônico", 5, "Dell", "Notebook potente", loja.getCpfCnpj());

        Optional<Produto> encontrado = facade.buscarProdutoPorNome("Notebook");
        assertTrue(encontrado.isPresent());
        assertEquals("Notebook", encontrado.get().getNome());
    }

    @Test
    void testListarProdutosDaLojaPorNome() {
        facade.cadastrarLoja("Loja XYZ", "xyz@teste.com", "senha123", "11122233344", "Av. Loja, 100");
        facade.cadastrarProduto("Produto XYZ", 10.0, "Tipo", 1, "Marca", "Descricao", "11122233344");

        List<Produto> produtos = facade.listarProdutosDaLojaNome("Loja XYZ");
        assertEquals(1, produtos.size());
    }

    @Test
    void testAtualizarProduto() {
        facade.cadastrarLoja(loja.getNome(), loja.getEmail(), loja.getSenha(), loja.getCpfCnpj(), loja.getEndereco());
        Produto produto = facade.cadastrarProduto("Celular", 1000.0, "Eletrônico", 5, "MarcaX", "Desc", loja.getCpfCnpj());

        Produto atualizado = facade.atualizarProduto(produto.getId(), "Celular Premium", 1500.0, "Eletrônico", 10, "MarcaX", "Melhorado", loja.getCpfCnpj());
        assertEquals("Celular Premium", atualizado.getNome());
        assertEquals(1500.0, atualizado.getValor());
        assertEquals(10, atualizado.getQuantidade());
    }

    @Test
    void testAdicionarPontuacao() {
        facade.cadastrarComprador(comprador.getNome(), comprador.getEmail(), comprador.getSenha(), comprador.getCpf(), comprador.getEndereco());
        facade.adicionarPontuacao(20, comprador.getCpf());

        Optional<Comprador> c = facade.buscarComprador(comprador.getCpf());
        assertEquals(20, c.get().getPontuacao());
    }

    @Test
    void testDecrementarPontuacao() {
        facade.cadastrarComprador(comprador.getNome(), comprador.getEmail(), comprador.getSenha(), comprador.getCpf(), comprador.getEndereco());
        facade.adicionarPontuacao(20, comprador.getCpf());
        facade.decrementarPontuacao(comprador.getCpf());

        Optional<Comprador> c = facade.buscarComprador(comprador.getCpf());
        assertEquals(8, c.get().getPontuacao()); // 20 - 12
    }

    @Test
    void testAtualizarComprador() {
        facade.cadastrarComprador(comprador.getNome(), comprador.getEmail(), comprador.getSenha(), comprador.getCpf(), comprador.getEndereco());
        comprador.setNome("Comprador Atualizado");

        Comprador atualizado = facade.atualizarComprador(comprador);
        assertEquals("Comprador Atualizado", atualizado.getNome());
    }

    @Test
    void testCadastrarAvaliacaoEListar() {
        facade.cadastrarComprador(comprador.getNome(), comprador.getEmail(), comprador.getSenha(), comprador.getCpf(), comprador.getEndereco());
        facade.cadastrarLoja(loja.getNome(), loja.getEmail(), loja.getSenha(), loja.getCpfCnpj(), loja.getEndereco());

        Produto produto = facade.cadastrarProduto("Produto Avaliado", 100.0, "Tipo", 1, "Marca", "Desc", loja.getCpfCnpj());
        String historicoId = facade.registrarCompra(comprador.getCpf(), List.of(produto), 100.0);

        facade.cadastrarAvaliacao(comprador.getCpf(), historicoId, 5, "Ótimo!", loja.getCpfCnpj());

        List<Avaliacao> avaliacoes = facade.listarAvaliacoesPorCompra(historicoId);
        assertEquals(1, avaliacoes.size());
        assertEquals(5, avaliacoes.get(0).getNota());
    }
}
