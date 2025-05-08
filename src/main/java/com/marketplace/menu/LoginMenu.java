package com.marketplace.menu;

import com.marketplace.facade.MarketplaceFacade;
import com.marketplace.model.Admin;
import com.marketplace.model.Comprador;
import com.marketplace.model.Loja;

import java.util.Optional;
import java.util.Scanner;

public class LoginMenu extends Menu{
    private MarketplaceFacade facade;

    protected LoginMenu(Scanner scanner, MarketplaceFacade facade) {
        super(scanner, "LOGIN");
        this.facade = facade;
    }

    @Override
    protected void initializeOptions() {
        addOption(1, new MenuOption() {
            public String getDescription() { return "Loja"; }
            public void execute() { loginLoja(); }
        });

        addOption(2, new MenuOption() {
            public String getDescription() { return "Comprador"; }
            public void execute() { loginComprador(); }
        });

        addOption(3, new MenuOption() {
            public String getDescription() { return "Administrador"; }
            public void execute() { loginAdmin(); }
        });

        addOption(0, new MenuOption() {
            public String getDescription() { return "Voltar"; }
            public void execute() { }
        });
    }

    private void loginLoja() {
        int option = 1;

        do {
            System.out.println("\n=== LOGIN LOJA ===");
            System.out.print("CPF/CNPJ: ");
            String cpfCnpj = scanner.nextLine();
            System.out.print("Senha: ");
            String senha = scanner.nextLine();

            Optional<Loja> loja = facade.buscarLoja(cpfCnpj);

            if (loja.isEmpty()) {
                System.out.println("Loja não encontrada");
                return;
            } else {
                if (loja.get().getCpfCnpj().equals(cpfCnpj) && loja.get().getSenha().equals(senha)) {
                    System.out.println("Login realizado com sucesso, Bem vindo(a) " + loja.get().getNome());
                    new LojaLoginMenu(scanner, facade, loja.get()).show();
                    option = 0;
                } else {
                    System.out.println("Senha Incorreta!");
                    System.out.println("\n=== OPÇÕES ===");
                    System.out.print("1. Tentar Novamente");
                    System.out.print("0. Sair");
                    option = scanner.nextInt();
                    scanner.nextLine(); // Consume newline
                }
            }
        }while (option != 0);
    }

    private void loginComprador() {
        int option = 1;

        do {
            System.out.println("\n=== LOGIN COMPRADOR ===");
            System.out.print("CPF: ");
            String cpf = scanner.nextLine();
            System.out.print("Senha: ");
            String senha = scanner.nextLine();

            Optional<Comprador> comprador = facade.buscarComprador(cpf);

            if (comprador.isEmpty()) {
                System.out.println("Comprador não encontrado");
                return;
            } else {
                if (comprador.get().getCpf().equals(cpf) && comprador.get().getSenha().equals(senha)) {
                    System.out.println("Login realizado com sucesso, Bem vindo(a) " + comprador.get().getNome());
                    new CompradorLoginMenu(scanner, facade, comprador.get()).show();
                    option = 0;
                } else {
                    System.out.println("Senha Incorreta!");
                    System.out.println("\n=== OPÇÕES ===");
                    System.out.println("1. Tentar Novamente");
                    System.out.println("0. Sair");
                    System.out.print("Escolha uma opção: ");
                    option = scanner.nextInt();
                    scanner.nextLine(); // Consume newline
                }
            }
        }while (option != 0);
    }

    private void loginAdmin() {
        int option = 1;

        do {
            System.out.println("\n=== LOGIN ADMIN ===");
            System.out.print("CPF: ");
            String cpf = scanner.nextLine();
            System.out.print("Senha: ");
            String senha = scanner.nextLine();

            Optional<Admin> admin = facade.buscarAdmin(cpf);

            if (admin.isEmpty()) {
                System.out.println("Admin não encontrado");
                return;
            } else {
                if (admin.get().getCpf().equals(cpf) && admin.get().getSenha().equals(senha)) {
                    System.out.println("Login realizado com sucesso, Bem vindo(a) " + admin.get().getNome());
                    new AdminLoginMenu(scanner, facade).show();
                    option = 0;
                } else {
                    System.out.println("Senha Incorreta!");
                    System.out.println("\n=== OPÇÕES ===");
                    System.out.print("1. Tentar Novamente");
                    System.out.print("0. Sair");
                    option = scanner.nextInt();
                    scanner.nextLine(); // Consume newline
                }
            }
        }while (option != 0);
    }
}
