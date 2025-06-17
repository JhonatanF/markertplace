package com.marketplace.facade;

import com.marketplace.model.Admin;
import com.marketplace.model.Avaliacao;
import com.marketplace.model.Comprador;
import com.marketplace.model.Historico;
import com.marketplace.model.Loja;
import com.marketplace.model.Produto;
import com.marketplace.repository.AdminRepository;
import com.marketplace.repository.AvaliacaoRepository;
import com.marketplace.repository.CompradorRepository;
import com.marketplace.repository.HistoricoRepository;
import com.marketplace.repository.LojaRepository;
import com.marketplace.repository.ProdutoRepository;
import com.marketplace.service.AdminService;
import com.marketplace.service.AvaliacaoService;
import com.marketplace.service.CompradorService;
import com.marketplace.service.HistoricoService;
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
        facade.removerHistoricos();
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
        Comprador compradorAtualizado = new Comprador(novoNome, comprador.getEmail(), 
                                                   comprador.getSenha(), comprador.getCpf(), 
                                                   comprador.getEndereco());
        Comprador atualizado = facade.atualizarComprador(compradorAtualizado);
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
        // Cadastrar a loja primeiro, mas sem produtos
        facade.cadastrarLoja(loja.getNome(), loja.getEmail(), loja.getSenha(), loja.getCpfCnpj(), loja.getEndereco());

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

    @Test
    void testBuscarLojaNome() {
        // Cadastrar uma loja
        facade.cadastrarLoja(loja.getNome(), loja.getEmail(), loja.getSenha(), loja.getCpfCnpj(), loja.getEndereco());

        // Buscar por nome
        List<Loja> lojas = facade.buscarLojaNome(loja.getNome());

        // Verificar se encontrou
        assertFalse(lojas.isEmpty(), "Deve encontrar a loja pelo nome");
        assertEquals(loja.getNome(), lojas.get(0).getNome(), "O nome da loja deve corresponder");
    }

    @Test
    void testAdicionarPontuacao() {
        // Cadastrar um comprador
        facade.cadastrarComprador(comprador.getNome(), comprador.getEmail(), comprador.getSenha(), comprador.getCpf(), comprador.getEndereco());

        // Adicionar pontuação
        int pontuacaoInicial = comprador.getPontuacao();
        int pontuacaoAdicional = 10;
        facade.adicionarPontuacao(pontuacaoAdicional, comprador.getCpf());

        // Verificar se a pontuação foi adicionada
        Optional<Comprador> compradorAtualizado = facade.buscarComprador(comprador.getCpf());
        assertTrue(compradorAtualizado.isPresent(), "Comprador deve existir");
        assertEquals(pontuacaoInicial + pontuacaoAdicional, compradorAtualizado.get().getPontuacao(), 
                "A pontuação deve ser incrementada corretamente");
    }

    @Test
    void testDecrementarPontuacao() {
        // Cadastrar um comprador
        Comprador compradorCadastrado = facade.cadastrarComprador(comprador.getNome(), comprador.getEmail(), 
                                                               comprador.getSenha(), comprador.getCpf(), 
                                                               comprador.getEndereco());

        // Adicionar pontuação primeiro para garantir que temos pontos suficientes
        int pontuacaoAdicional = 20;
        facade.adicionarPontuacao(pontuacaoAdicional, comprador.getCpf());

        // Decrementar pontuação
        facade.decrementarPontuacao(comprador.getCpf());

        // Verificar se a pontuação foi decrementada
        Optional<Comprador> compradorAtualizado = facade.buscarComprador(comprador.getCpf());
        assertTrue(compradorAtualizado.isPresent(), "Comprador deve existir");
        assertEquals(pontuacaoAdicional - 12, compradorAtualizado.get().getPontuacao(), 
                "A pontuação deve ser decrementada em 12 pontos");
    }

    @Test
    void testBuscarProdutoPorNome() {
        // Cadastrar loja e produto
        facade.cadastrarLoja(loja.getNome(), loja.getEmail(), loja.getSenha(), loja.getCpfCnpj(), loja.getEndereco());
        Produto produtoCadastrado = facade.cadastrarProduto(produto.getNome(), produto.getValor(), produto.getTipo(),
                produto.getQuantidade(), produto.getMarca(), produto.getDescricao(), produto.getLojaCpfCnpj());

        // Buscar produto por nome
        Optional<Produto> produtoEncontrado = facade.buscarProdutoPorNome(produto.getNome());

        // Verificar se encontrou
        assertTrue(produtoEncontrado.isPresent(), "Deve encontrar o produto pelo nome");
        assertEquals(produto.getNome(), produtoEncontrado.get().getNome(), "O nome do produto deve corresponder");
    }

    @Test
    void testListarProdutosDaLojaNome() {
        // Cadastrar loja e produto
        facade.cadastrarLoja(loja.getNome(), loja.getEmail(), loja.getSenha(), loja.getCpfCnpj(), loja.getEndereco());
        Produto produtoCadastrado = facade.cadastrarProduto(produto.getNome(), produto.getValor(), produto.getTipo(),
                produto.getQuantidade(), produto.getMarca(), produto.getDescricao(), produto.getLojaCpfCnpj());

        // Listar produtos da loja pelo nome
        List<Produto> produtos = facade.listarProdutosDaLojaNome(loja.getNome());

        // Verificar se listou corretamente
        assertFalse(produtos.isEmpty(), "Deve listar produtos da loja");
        assertEquals(1, produtos.size(), "Deve haver um produto na loja");
        assertEquals(produto.getNome(), produtos.get(0).getNome(), "O nome do produto deve corresponder");
    }

    @Test
    void testAtualizarProduto() {
        // Cadastrar loja e produto
        facade.cadastrarLoja(loja.getNome(), loja.getEmail(), loja.getSenha(), loja.getCpfCnpj(), loja.getEndereco());
        Produto produtoCadastrado = facade.cadastrarProduto(produto.getNome(), produto.getValor(), produto.getTipo(),
                produto.getQuantidade(), produto.getMarca(), produto.getDescricao(), produto.getLojaCpfCnpj());

        // Atualizar produto
        String novoNome = "Produto Atualizado";
        double novoValor = 149.99;
        Produto produtoAtualizado = facade.atualizarProduto(
                produtoCadastrado.getId(), novoNome, novoValor, produto.getTipo(),
                produto.getQuantidade(), produto.getMarca(), produto.getDescricao(), produto.getLojaCpfCnpj());

        // Verificar se atualizou corretamente
        assertEquals(novoNome, produtoAtualizado.getNome(), "O nome do produto deve ser atualizado");
        assertEquals(novoValor, produtoAtualizado.getValor(), "O valor do produto deve ser atualizado");
    }

    @Test
    void testRegistrarCompraEBuscarHistorico() {
        // Criar um comprador diferente para este teste para garantir isolamento
        String compradorCpf = "11122233344";
        Comprador compradorTeste = new Comprador("Comprador Teste 2", "comprador2@teste.com", "senha123", compradorCpf, "Rua Comprador 2, 789");

        // Cadastrar comprador, loja e produto
        facade.cadastrarComprador(compradorTeste.getNome(), compradorTeste.getEmail(), compradorTeste.getSenha(), compradorTeste.getCpf(), compradorTeste.getEndereco());
        facade.cadastrarLoja(loja.getNome(), loja.getEmail(), loja.getSenha(), loja.getCpfCnpj(), loja.getEndereco());
        Produto produtoCadastrado = facade.cadastrarProduto(produto.getNome(), produto.getValor(), produto.getTipo(),
                produto.getQuantidade(), produto.getMarca(), produto.getDescricao(), produto.getLojaCpfCnpj());

        // Criar lista de produtos para compra
        List<Produto> produtosCompra = List.of(produtoCadastrado);
        double valorTotal = produtoCadastrado.getValor();

        // Registrar compra
        String idCompra = facade.registrarCompra(compradorTeste.getCpf(), produtosCompra, valorTotal);

        // Verificar se a compra foi registrada
        assertNotNull(idCompra, "O ID da compra não deve ser nulo");

        // Buscar histórico do comprador
        Historico historico = facade.buscarHistoricoComprador(compradorTeste.getCpf());

        // Verificar se o histórico foi encontrado
        assertNotNull(historico, "O histórico do comprador deve existir");
        assertFalse(historico.getCompras().isEmpty(), "O histórico deve conter compras");
        assertEquals(1, historico.getCompras().size(), "Deve haver uma compra no histórico");
        assertEquals(idCompra, historico.getCompras().get(0).getId(), "O ID da compra deve corresponder");
        assertEquals(valorTotal, historico.getCompras().get(0).getTotal(), "O valor total deve corresponder");
    }

    @Test
    void testAvaliacaoFluxoCompleto() {
        // Cadastrar comprador, loja e produto
        facade.cadastrarComprador(comprador.getNome(), comprador.getEmail(), comprador.getSenha(), comprador.getCpf(), comprador.getEndereco());
        facade.cadastrarLoja(loja.getNome(), loja.getEmail(), loja.getSenha(), loja.getCpfCnpj(), loja.getEndereco());
        Produto produtoCadastrado = facade.cadastrarProduto(produto.getNome(), produto.getValor(), produto.getTipo(),
                produto.getQuantidade(), produto.getMarca(), produto.getDescricao(), produto.getLojaCpfCnpj());

        // Criar lista de produtos para compra
        List<Produto> produtosCompra = List.of(produtoCadastrado);
        double valorTotal = produtoCadastrado.getValor();

        // Registrar compra
        String idCompra = facade.registrarCompra(comprador.getCpf(), produtosCompra, valorTotal);

        // Cadastrar avaliação
        int nota = 5;
        String comentario = "Ótimo produto, recomendo!";
        Avaliacao avaliacao = facade.cadastrarAvaliacao(comprador.getCpf(), idCompra, nota, comentario, loja.getCpfCnpj());

        // Verificar se a avaliação foi cadastrada
        assertNotNull(avaliacao, "A avaliação não deve ser nula");
        assertEquals(nota, avaliacao.getNota(), "A nota deve corresponder");
        assertEquals(comentario, avaliacao.getComentario(), "O comentário deve corresponder");

        // Listar avaliações por compra
        List<Avaliacao> avaliacoes = facade.listarAvaliacoesPorCompra(idCompra);

        // Verificar se a lista de avaliações foi retornada corretamente
        assertFalse(avaliacoes.isEmpty(), "A lista de avaliações não deve estar vazia");
        assertEquals(1, avaliacoes.size(), "Deve haver uma avaliação na lista");
        assertEquals(avaliacao.getId(), avaliacoes.get(0).getId(), "O ID da avaliação deve corresponder");

        // Calcular conceito da loja
        String conceito = facade.calcularConceitoLoja(loja.getCpfCnpj());

        // Verificar se o conceito foi calculado corretamente
        assertNotNull(conceito, "O conceito não deve ser nulo");
        assertTrue(conceito.contains("5.0"), "O conceito deve conter a nota média 5.0");
        assertTrue(conceito.contains("Ótima"), "O conceito deve conter a classificação 'Ótima'");
    }
}
