package com.marketplace.facade;

import com.marketplace.model.Admin;
import com.marketplace.model.Comprador;
import com.marketplace.model.Loja;
import com.marketplace.model.Produto;
import com.marketplace.service.AdminService;
import com.marketplace.service.CompradorService;
import com.marketplace.service.LojaService;
import com.marketplace.service.ProdutoService;

import java.util.List;
import java.util.Optional;

public class MarketplaceFacade {
    private final LojaService lojaService;
    private final CompradorService compradorService;
    private final ProdutoService produtoService;
    private final AdminService adminService;

    public MarketplaceFacade(LojaService lojaService, CompradorService compradorService, ProdutoService produtoService, AdminService adminService) {
        this.lojaService = lojaService;
        this.compradorService = compradorService;
        this.produtoService = produtoService;
        this.adminService = adminService;
    }

    // Operações de Loja
    public Loja cadastrarLoja(String nome, String email, String senha, String cpfCnpj, String endereco) {
        Loja loja = new Loja(nome, email, senha, cpfCnpj, endereco);
        return lojaService.cadastrar(loja);
    }

    public Optional<Loja> buscarLoja(String cpfCnpj) {
        return lojaService.buscarPorId(cpfCnpj);
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

    public Comprador atualizarComprador(String nome, String email, String senha, String cpf, String endereco) {
        Comprador comprador = new Comprador(nome, email, senha, cpf, endereco);
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
            System.out.println(produto.getNome() + " | valor:" + produto.getValor() + " | estoque:" + produto.getQuantidade() + " | id:" + produto.getId());
        }
        System.out.println();
        return produtosDaLoja;
    }

    public Produto atualizarProduto(String id, String nome, double valor, String tipo, 
                                   int quantidade, String marca, String descricao, String lojaCpfCnpj) {
        Produto produto = new Produto(id,nome, valor, tipo, quantidade, marca, descricao, lojaCpfCnpj);
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

    // Operações de remoção geral

    protected void removerAdmins() {
        adminService.removerAdmins();
    }

    protected void removerCompradores () {
        compradorService.removerCompradores();
    }

    protected void removerProdutos () {
        produtoService.removerProdutos();
    }

    protected void removerLojas () {
        lojaService.removerLojas();
    }


}
