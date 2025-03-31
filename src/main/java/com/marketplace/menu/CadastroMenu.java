package com.marketplace.menu;

import com.marketplace.facade.MarketplaceFacade;
import com.marketplace.model.Admin;
import com.marketplace.model.Comprador;
import com.marketplace.model.Loja;

import java.util.Scanner;

public class CadastroMenu extends Menu{
    private MarketplaceFacade facade;

    protected CadastroMenu(Scanner scanner, MarketplaceFacade facade) {
        super(scanner, "CADASTRO");
        this.facade = facade;
    }

    @Override
    protected void initializeOptions() {
        addOption(1, new MenuOption() {
            public String getDescription() { return "Loja"; }
            public void execute() { cadastrarLoja(); }
        });

        addOption(2, new MenuOption() {
            public String getDescription() { return "Comprador"; }
            public void execute() { cadastrarComprador(); }
        });

        addOption(3, new MenuOption() {
            public String getDescription() { return "Administrador"; }
            public void execute() { cadastrarAdmin(); }
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

    private void cadastrarComprador() {
        System.out.println("\n=== CADASTRAR COMPRADOR ===");
        System.out.print("Nome: ");
        String nome = scanner.nextLine();
        System.out.print("Email: ");
        String email = scanner.nextLine();
        System.out.print("Senha: ");
        String senha = scanner.nextLine();
        System.out.print("CPF: ");
        String cpf = scanner.nextLine();
        System.out.print("Endereço: ");
        String endereco = scanner.nextLine();

        Comprador comprador = facade.cadastrarComprador(nome, email, senha, cpf, endereco);
        System.out.println("Comprador cadastrado com sucesso: " + comprador.getNome());
    }

    public void cadastrarAdmin() {
        System.out.println("\n=== CADASTRAR ADMINISTRADOR ===");
        System.out.print("Nome: ");
        String nome = scanner.nextLine();
        System.out.print("Email: ");
        String email = scanner.nextLine();
        System.out.print("Senha: ");
        String senha = scanner.nextLine();
        System.out.print("CPF: ");
        String cpf = scanner.nextLine();
        System.out.print("Endereço: ");
        String endereco = scanner.nextLine();

        Admin administrador = facade.cadastrarAdmin(nome, email, senha, cpf, endereco);
        System.out.println("Administrador cadastrado com sucesso: " + administrador.getNome());
    }
}
