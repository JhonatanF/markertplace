package com.marketplace.menu;

import com.marketplace.facade.MarketplaceFacade;
import com.marketplace.model.Admin;

import java.util.Scanner;

public class AdminMenu extends Menu {
    private final MarketplaceFacade facade;

    public AdminMenu(Scanner scanner, MarketplaceFacade facade) {
        super(scanner, "MENU ADMINISTRADOR");
        this.facade = facade;
    }

    @Override
    protected void initializeOptions() {
        addOption(1, new MenuOption() {
            @Override
            public String getDescription() {
                return "Cadastrar administrador";
            }

            @Override
            public void execute() {
                cadastrarAdmin();
            }
        });

        addOption(2, new MenuOption() {
            @Override
            public String getDescription() {
                return "Buscar administrador";
            }

            @Override
            public void execute() {
                buscarAdmin();
            }
        });

        addOption(3, new MenuOption() {
            @Override
            public String getDescription() {
                return "Listar administradores";
            }

            @Override
            public void execute() {
                listarAdmins();
            }
        });

        addOption(4, new MenuOption() {
            @Override
            public String getDescription() {
                return "Atualizar administrador";
            }

            @Override
            public void execute() {
                atualizarAdmin();
            }
        });

        addOption(5, new MenuOption() {
            @Override
            public String getDescription() {
                return "Remover administrador";
            }

            @Override
            public void execute() {
                removerAdmin();
            }
        });

        addOption(0, new MenuOption() {
            @Override
            public String getDescription() {
                return "Voltar";
            }

            @Override
            public void execute() { }
        });
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

    private void buscarAdmin() {
        System.out.print("Digite o CPF do administrador: ");
        String cpf = scanner.nextLine();
        facade.buscarAdmin(cpf).ifPresentOrElse(
                admin -> System.out.println(admin),
                () -> System.out.println("Administrador não encontrado")
        );
    }

    private void listarAdmins() {
        var admins = facade.listarAdmins();
        if (admins.isEmpty()) {
            System.out.println("Nenhum administrador cadastrado");
            return;
        }
        admins.forEach(System.out::println);
    }

    private void atualizarAdmin() {
        System.out.print("Digite o CPF do administrador: ");
        String cpf = scanner.nextLine();

        if (facade.buscarAdmin(cpf).isEmpty()) {
            System.out.println("Administrador não encontrado");
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

        Admin admin = facade.atualizarAdmin(nome, email, senha, cpf, endereco);
        System.out.println("Administrador atualizado com sucesso: " + admin.getNome());
    }

    private void removerAdmin() {
        System.out.print("Digite o CPF do administrador: ");
        String cpf = scanner.nextLine();
        facade.removerAdmin(cpf);
        System.out.println("Administrador removido com sucesso");
    }
}
