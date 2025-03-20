package com.marketplace.menu;

import com.marketplace.facade.MarketplaceFacade;
import com.marketplace.model.Produto;
import java.util.Scanner;

public class ProdutoMenu extends Menu {
    private final MarketplaceFacade facade;

    public ProdutoMenu(Scanner scanner, MarketplaceFacade facade) {
        super(scanner, "GERENCIAR PRODUTOS");
        this.facade = facade;
    }

    @Override
    protected void initializeOptions() {
        addOption(1, new MenuOption() {
            public String getDescription() { return "Cadastrar Produto"; }
            public void execute() { cadastrarProduto(); }
        });

        addOption(2, new MenuOption() {
            public String getDescription() { return "Buscar Produto"; }
            public void execute() { buscarProduto(); }
        });

        addOption(3, new MenuOption() {
            public String getDescription() { return "Listar Produtos"; }
            public void execute() { listarProdutos(); }
        });

        addOption(4, new MenuOption() {
            public String getDescription() { return "Listar Produtos por Loja"; }
            public void execute() { listarProdutosPorLoja(); }
        });

        addOption(5, new MenuOption() {
            public String getDescription() { return "Atualizar Produto"; }
            public void execute() { atualizarProduto(); }
        });

        addOption(6, new MenuOption() {
            public String getDescription() { return "Remover Produto"; }
            public void execute() { removerProduto(); }
        });

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
        System.out.print("CPF/CNPJ da Loja: ");
        String lojaCpfCnpj = scanner.nextLine();

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
        var produtos = facade.listarProdutos();
        if (produtos.isEmpty()) {
            System.out.println("Nenhum produto cadastrado");
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
        System.out.print("CPF/CNPJ da Loja: ");
        String lojaCpfCnpj = scanner.nextLine();

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
