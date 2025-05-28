package com.marketplace.facade;

import com.marketplace.model.Admin;
import com.marketplace.model.Comprador;
import com.marketplace.model.Historico;
import com.marketplace.model.Loja;
import com.marketplace.model.Produto;
import com.marketplace.model.Avaliacao; // Importação da Avaliacao
import com.marketplace.service.AdminService;
import com.marketplace.service.CompradorService;
import com.marketplace.service.HistoricoService;
import com.marketplace.service.LojaService;
import com.marketplace.service.ProdutoService;
import com.marketplace.service.AvaliacaoService; // Importação do serviço de Avaliação

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class MarketplaceFacade {
    private final LojaService lojaService;
    private final CompradorService compradorService;
    private final ProdutoService produtoService;
    private final AdminService adminService;
    private final HistoricoService historicoService;
    private final AvaliacaoService avaliacaoService; // Novo serviço

    public MarketplaceFacade(LojaService lojaService, CompradorService compradorService, ProdutoService produtoService,
            AdminService adminService, HistoricoService historicoService, AvaliacaoService avaliacaoService) {
        this.lojaService = lojaService;
        this.compradorService = compradorService;
        this.produtoService = produtoService;
        this.adminService = adminService;
        this.historicoService = historicoService;
        this.avaliacaoService = avaliacaoService; // Inicialização do novo serviço
    }

    // Operações de Loja
    public Loja cadastrarLoja(String nome, String email, String senha, String cpfCnpj, String endereco) {
        Loja loja = new Loja(nome, email, senha, cpfCnpj, endereco);
        return lojaService.cadastrar(loja);
    }

    public Optional<Loja> buscarLoja(String cpfCnpj) {
        return lojaService.buscarPorId(cpfCnpj);
    }


    public List<Loja> buscarLojaNome(String nome) {
        return lojaService.buscarPorNome(nome);
    }

    public List<Loja> listarLojas() {
        return lojaService.listarTodas();
    }

    public Loja atualizarLoja(String nome, String email, String senha, String cpfCnpj, String endereco) {
        Loja loja = new Loja(nome, email, senha, cpfCnpj, endereco);
        return lojaService.atualizar(loja);
    }

    public void removerLoja(String cpfCnpj) {
        lojaService.remover(cpfCnpj);
    }

    // Operações de Comprador
    public Comprador cadastrarComprador(String nome, String email, String senha, String cpf, String endereco) {
        Comprador comprador = new Comprador(nome, email, senha, cpf, endereco);
        return compradorService.cadastrar(comprador);
    }

    public Optional<Comprador> buscarComprador(String cpf) {
        return compradorService.buscarPorId(cpf);
    }

    public List<Comprador> listarCompradores() {
        return compradorService.listarTodos();
    }

    public void adicionarPontuacao(int pontuacao, String cpf) {
        Optional<Comprador> compradorOptional = compradorService.buscarPorId(cpf);
        Comprador compradorAtualizado = compradorOptional.get();
        compradorAtualizado.setPontuacao(compradorAtualizado.getPontuacao() + pontuacao);
        compradorService.atualizar(compradorAtualizado);
    }

    public void decrementarPontuacao(String cpf) {
        Optional<Comprador> compradorOptional = compradorService.buscarPorId(cpf);
        Comprador compradorAtualizado = compradorOptional.get();
        compradorAtualizado.setPontuacao(compradorAtualizado.getPontuacao() - 12);
        compradorService.atualizar(compradorAtualizado);
    }

    public Comprador atualizarComprador(Comprador comprador) {
        // Valida se o comprador existe
        if (!compradorService.buscarPorId(comprador.getCpf()).isPresent()) {
            throw new IllegalArgumentException("Comprador não encontrado");
        }

        // Atualiza o comprador diretamente
        return compradorService.atualizar(comprador);
    }

    public void removerComprador(String cpf) {
        compradorService.remover(cpf);
    }

    // Operações de Produto
    public Produto cadastrarProduto(String nome, double valor, String tipo, int quantidade,
            String marca, String descricao, String lojaCpfCnpj) {
        Produto produto = new Produto(nome, valor, tipo, quantidade, marca, descricao, lojaCpfCnpj);
        return produtoService.cadastrar(produto);
    }

    public Optional<Produto> buscarProduto(String id) {
        return produtoService.buscarPorId(id);
    }

    public Optional<Produto> buscarProdutoPorNome(String nome) {
        for (Produto prod : produtoService.listarTodos()) {
            if (prod.getNome().equalsIgnoreCase(nome)) {
                return Optional.of(prod);
            }
        }
        return Optional.empty();
    }

    public List<Produto> listarProdutos() {
        return produtoService.listarTodos();
    }

    public List<Produto> listarProdutosDaLoja(String lojaCpfCnpj) {
        List<Produto> produtosDaLoja = produtoService.listarPorLoja(lojaCpfCnpj);
        Optional<Loja> busca = buscarLoja(lojaCpfCnpj);
        Loja loja = busca.get();

        System.out.println("\nPRODUTOS DA LOJA: " + loja.getNome());
        for (Produto produto : produtosDaLoja) {
            System.out.println(produto.getNome() + " | valor:" + produto.getValor() + " | estoque:"
                    + produto.getQuantidade() + " | id:" + produto.getId());
        }
        System.out.println();
        return produtosDaLoja;
    }

    public List<Produto> listarProdutosDaLojaNome(String nomeLoja) {
        List<Loja> lojas = listarLojas().stream()
                .filter(loja -> loja.getNome().equalsIgnoreCase(nomeLoja))
                .collect(Collectors.toList());

        if (lojas.isEmpty()) {
            System.out.println("Loja não encontrada.");
            return List.of();
        }

        Loja loja = lojas.get(0);

        List<Produto> produtosDaLoja = produtoService.listarPorLoja(loja.getCpfCnpj());

        System.out.println("\nPRODUTOS DA LOJA: " + loja.getNome());
        for (Produto produto : produtosDaLoja) {
            System.out.println(produto.getNome() + " | valor: " + produto.getValor()
                    + " | estoque: " + produto.getQuantidade()
                    + " | id: " + produto.getId());
        }
        System.out.println();
        return produtosDaLoja;
    }


    public Produto atualizarProduto(String id, String nome, double valor, String tipo,
            int quantidade, String marca, String descricao, String lojaCpfCnpj) {
        Produto produto = new Produto(id, nome, valor, tipo, quantidade, marca, descricao, lojaCpfCnpj);
        return produtoService.atualizar(produto);
    }

    public void removerProduto(String id) {
        produtoService.remover(id);
    }

    // Operações de Administrador
    public Admin cadastrarAdmin(String nome, String email, String senha, String cpf, String endereco) {
        Admin admin = new Admin(nome, email, senha, cpf, endereco);
        return adminService.cadastrar(admin);
    }

    public Optional<Admin> buscarAdmin(String cpf) {
        return adminService.buscarPorId(cpf);
    }

    public List<Admin> listarAdmins() {
        return adminService.listarTodos();
    }

    public Admin atualizarAdmin(String nome, String email, String senha, String cpf, String endereco) {
        Admin admin = new Admin(nome, email, senha, cpf, endereco);
        return adminService.atualizar(admin);
    }

    public void removerAdmin(String id) {
        adminService.remover(id);
    }

    public String registrarCompra(String compradorCpf, List<Produto> produtos, double total) {
        Historico.Compra novaCompra = new Historico.Compra(produtos, total);
        List<Historico.Compra> compras = new ArrayList<>();
        compras.add(novaCompra);

        historicoService.cadastrarOuAtualizarHistorico(compradorCpf, compras);
        return novaCompra.getId(); // Retorna o ID da compra
    }

    public Historico buscarHistoricoComprador(String compradorCpf) {
        return historicoService.buscarPorComprador(compradorCpf);
    }

    // Operações de remoção geral
    protected void removerAdmins() {
        adminService.removerAdmins();
    }

    protected void removerCompradores() {
        compradorService.removerCompradores();
    }

    protected void removerProdutos() {
        produtoService.removerProdutos();
    }

    protected void removerLojas() {
        lojaService.removerLojas();
    }

    public Avaliacao cadastrarAvaliacao(String compradorCpf, String historicoId, int nota, String comentario,
            String idLoja) {
        Avaliacao avaliacao = new Avaliacao(compradorCpf, historicoId, nota, comentario, idLoja);
        return avaliacaoService.cadastrar(avaliacao);
    }

    public List<Avaliacao> listarAvaliacoesPorCompra(String historicoId) {
        return avaliacaoService.listarPorCompra(historicoId);
    }

    public String calcularConceitoLoja(String idLoja) {
        return avaliacaoService.calcularMediaAvaliacoesLoja(idLoja);
    }
}
