package com.marketplace.menu;

import com.marketplace.facade.MarketplaceFacade;
import com.marketplace.model.Loja;
import com.marketplace.model.Produto;

import java.util.List;
import java.util.Scanner;

public class ProdutoMenu extends Menu {
    private final MarketplaceFacade facade;
    private final Loja loja;

    public ProdutoMenu(Scanner scanner, MarketplaceFacade facade, Loja loja) {
        super(scanner, "GERENCIAR PRODUTOS");
        this.facade = facade;
        this.loja = loja;
        System.out.println(loja);
    }

    public ProdutoMenu(Scanner scanner, MarketplaceFacade facade) {
        super(scanner, "GERENCIAR PRODUTOS");
        this.facade = facade;
        this.loja = null;
    }

    @Override
    protected void initializeOptions() {
        int key = 1;
        addOption(key, new MenuOption() {
            public String getDescription() { return "Cadastrar Produto"; }
            public void execute() { cadastrarProduto(); }
        });
        key++;

        addOption(key, new MenuOption() {
            public String getDescription() { return "Buscar Produto"; }
            public void execute() { buscarProduto(); }
        });
        key++;

        addOption(key, new MenuOption() {
            public String getDescription() { return "Listar Produtos"; }
            public void execute() { listarProdutos(); }
        });
        key++;

        if (this.loja == null) { //Não necessário quando logado como loja
            addOption(key, new MenuOption() {
                public String getDescription() { return "Listar Produtos por Loja"; }
                public void execute() { listarProdutosPorLoja(); }
            });
            key++;
        }

        addOption(key, new MenuOption() {
            public String getDescription() { return "Atualizar Produto"; }
            public void execute() { atualizarProduto(); }
        });
        key++;

        addOption(key, new MenuOption() {
            public String getDescription() { return "Remover Produto"; }
            public void execute() { removerProduto(); }
        });
        key++;

        addOption(0, new MenuOption() {
            public String getDescription() { return "Voltar"; }
            public void execute() { }
        });
    }

    private void cadastrarProduto() {
        System.out.println("\n=== CADASTRAR PRODUTO ===");
        System.out.print("Nome: ");
        String nome = scanner.nextLine();
        System.out.print("Valor: ");
        double valor = scanner.nextDouble();
        scanner.nextLine(); // Consumir nova linha
        System.out.print("Tipo: ");
        String tipo = scanner.nextLine();
        System.out.print("Quantidade: ");
        int quantidade = scanner.nextInt();
        scanner.nextLine(); // Consumir nova linha
        System.out.print("Marca: ");
        String marca = scanner.nextLine();
        System.out.print("Descrição: ");
        String descricao = scanner.nextLine();

        String lojaCpfCnpj = "";

        if (loja == null) {
            System.out.print("CPF/CNPJ da Loja: ");
            lojaCpfCnpj = scanner.nextLine();
        }else{
            lojaCpfCnpj = loja.getCpfCnpj();
        }

        try {
            Produto produto = facade.cadastrarProduto(nome, valor, tipo, quantidade, marca, descricao, lojaCpfCnpj);
            System.out.println("Produto cadastrado com sucesso: " + produto.getNome());
        } catch (IllegalArgumentException e) {
            System.out.println("Erro ao cadastrar produto: " + e.getMessage());
        }
    }

    private void buscarProduto() {
        System.out.print("Digite o ID do produto: ");
        String id = scanner.nextLine();
        facade.buscarProduto(id).ifPresentOrElse(
            produto -> System.out.println(produto),
            () -> System.out.println("Produto não encontrado")
        );
    }

    private void listarProdutos() {
        List<Produto> produtos;
        if (loja == null) {
            produtos = facade.listarProdutos();
        }else {
            produtos = facade.listarProdutosDaLoja(loja.getCpfCnpj());
        }
            if (produtos.isEmpty()) {
                System.out.println("Nenhum produto encontrado para esta loja");
                return;
            }
            produtos.forEach(System.out::println);
    }

    private void listarProdutosPorLoja() {
        System.out.print("Digite o CPF/CNPJ da loja: ");
        String lojaCpfCnpj = scanner.nextLine();
        var produtos = facade.listarProdutosDaLoja(lojaCpfCnpj);
        if (produtos.isEmpty()) {
            System.out.println("Nenhum produto encontrado para esta loja");
            return;
        }
        produtos.forEach(System.out::println);
    }

    private void atualizarProduto() {
        System.out.print("Digite o ID do produto: ");
        String id = scanner.nextLine();
        
        if (facade.buscarProduto(id).isEmpty()) {
            System.out.println("Produto não encontrado");
            return;
        }

        System.out.print("Novo nome: ");
        String nome = scanner.nextLine();
        System.out.print("Novo valor: ");
        double valor = scanner.nextDouble();
        scanner.nextLine(); // Consumir nova linha
        System.out.print("Novo tipo: ");
        String tipo = scanner.nextLine();
        System.out.print("Nova quantidade: ");
        int quantidade = scanner.nextInt();
        scanner.nextLine(); // Consumir nova linha
        System.out.print("Nova marca: ");
        String marca = scanner.nextLine();
        System.out.print("Nova descrição: ");
        String descricao = scanner.nextLine();

        String lojaCpfCnpj = "";

        if (loja == null) {
            System.out.print("CPF/CNPJ da Loja: ");
            lojaCpfCnpj = scanner.nextLine();
        }else{
            lojaCpfCnpj = loja.getCpfCnpj();
        }

        try {
            Produto produto = facade.atualizarProduto(id, nome, valor, tipo, quantidade, marca, descricao, lojaCpfCnpj);
            System.out.println("Produto atualizado com sucesso: " + produto.getNome());
        } catch (IllegalArgumentException e) {
            System.out.println("Erro ao atualizar produto: " + e.getMessage());
        }
    }

    private void removerProduto() {
        System.out.print("Digite o ID do produto: ");
        String id = scanner.nextLine();
        facade.removerProduto(id);
        System.out.println("Produto removido com sucesso");
    }
}
