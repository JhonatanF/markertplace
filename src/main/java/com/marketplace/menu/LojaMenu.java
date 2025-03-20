package com.marketplace.menu;

import com.marketplace.facade.MarketplaceFacade;
import com.marketplace.model.Loja;
import java.util.Scanner;

public class LojaMenu extends Menu {
    private final MarketplaceFacade facade;

    public LojaMenu(Scanner scanner, MarketplaceFacade facade) {
        super(scanner, "GERENCIAR LOJAS");
        this.facade = facade;
    }

    @Override
    protected void initializeOptions() {
        addOption(1, new MenuOption() {
            public String getDescription() { return "Cadastrar Loja"; }
            public void execute() { cadastrarLoja(); }
        });

        addOption(2, new MenuOption() {
            public String getDescription() { return "Buscar Loja"; }
            public void execute() { buscarLoja(); }
        });

        addOption(3, new MenuOption() {
            public String getDescription() { return "Listar Lojas"; }
            public void execute() { listarLojas(); }
        });

        addOption(4, new MenuOption() {
            public String getDescription() { return "Atualizar Loja"; }
            public void execute() { atualizarLoja(); }
        });

        addOption(5, new MenuOption() {
            public String getDescription() { return "Remover Loja"; }
            public void execute() { removerLoja(); }
        });

        addOption(0, new MenuOption() {
            public String getDescription() { return "Voltar"; }
            public void execute() { }
        });
    }

    private void cadastrarLoja() {
        System.out.println("\n=== CADASTRAR LOJA ===");
        System.out.print("Nome: ");
        String nome = scanner.nextLine();
        System.out.print("Email: ");
        String email = scanner.nextLine();
        System.out.print("Senha: ");
        String senha = scanner.nextLine();
        System.out.print("CPF/CNPJ: ");
        String cpfCnpj = scanner.nextLine();
        System.out.print("Endereço: ");
        String endereco = scanner.nextLine();

        Loja loja = facade.cadastrarLoja(nome, email, senha, cpfCnpj, endereco);
        System.out.println("Loja cadastrada com sucesso: " + loja.getNome());
    }

    private void buscarLoja() {
        System.out.print("Digite o CPF/CNPJ da loja: ");
        String cpfCnpj = scanner.nextLine();
        facade.buscarLoja(cpfCnpj).ifPresentOrElse(
            loja -> System.out.println(loja),
            () -> System.out.println("Loja não encontrada")
        );
    }

    private void listarLojas() {
        var lojas = facade.listarLojas();
        if (lojas.isEmpty()) {
            System.out.println("Nenhuma loja cadastrada");
            return;
        }
        lojas.forEach(System.out::println);
    }

    private void atualizarLoja() {
        System.out.print("Digite o CPF/CNPJ da loja: ");
        String cpfCnpj = scanner.nextLine();
        
        if (facade.buscarLoja(cpfCnpj).isEmpty()) {
            System.out.println("Loja não encontrada");
            return;
        }

        System.out.print("Novo nome: ");
        String nome = scanner.nextLine();
        System.out.print("Novo email: ");
        String email = scanner.nextLine();
        System.out.print("Nova senha: ");
        String senha = scanner.nextLine();
        System.out.print("Novo endereço: ");
        String endereco = scanner.nextLine();

        Loja loja = facade.atualizarLoja(nome, email, senha, cpfCnpj, endereco);
        System.out.println("Loja atualizada com sucesso: " + loja.getNome());
    }

    private void removerLoja() {
        System.out.print("Digite o CPF/CNPJ da loja: ");
        String cpfCnpj = scanner.nextLine();
        facade.removerLoja(cpfCnpj);
        System.out.println("Loja removida com sucesso");
    }
}
