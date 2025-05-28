package com.marketplace.menu;

import com.marketplace.facade.MarketplaceFacade;
import com.marketplace.model.Comprador;

import java.util.Optional;
import java.util.Scanner;

public class CompradorMenu extends Menu {
    private final MarketplaceFacade facade;

    public CompradorMenu(Scanner scanner, MarketplaceFacade facade) {
        super(scanner, "GERENCIAR COMPRADORES");
        this.facade = facade;
    }

    @Override
    protected void initializeOptions() {
        addOption(1, new MenuOption() {
            public String getDescription() {
                return "Cadastrar Comprador";
            }

            public void execute() {
                cadastrarComprador();
            }
        });

        addOption(2, new MenuOption() {
            public String getDescription() {
                return "Buscar Comprador";
            }

            public void execute() {
                buscarComprador();
            }
        });

        addOption(3, new MenuOption() {
            public String getDescription() {
                return "Listar Compradores";
            }

            public void execute() {
                listarCompradores();
            }
        });

        addOption(4, new MenuOption() {
            public String getDescription() {
                return "Atualizar Comprador";
            }

            public void execute() {
                atualizarComprador();
            }
        });

        addOption(5, new MenuOption() {
            public String getDescription() {
                return "Remover Comprador";
            }

            public void execute() {
                removerComprador();
            }
        });

        addOption(0, new MenuOption() {
            public String getDescription() {
                return "Voltar";
            }

            public void execute() {
            }
        });
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

    private void buscarComprador() {
        System.out.print("Digite o CPF do comprador: ");
        String cpf = scanner.nextLine();
        facade.buscarComprador(cpf).ifPresentOrElse(
                comprador -> System.out.println(comprador),
                () -> System.out.println("Comprador não encontrado"));
    }

    private void listarCompradores() {
        var compradores = facade.listarCompradores();
        if (compradores.isEmpty()) {
            System.out.println("Nenhum comprador cadastrado");
            return;
        }
        compradores.forEach(System.out::println);
    }

    private void atualizarComprador() {
        System.out.print("Digite o CPF do comprador: ");
        String cpf = scanner.nextLine();

        Optional<Comprador> compradorOptional = facade.buscarComprador(cpf);

        if (compradorOptional.isEmpty()) {
            System.out.println("Comprador não encontrado.");
            return;
        }

        Comprador comprador = compradorOptional.get();

        System.out.print("Novo nome: ");
        String nome = scanner.nextLine();
        System.out.print("Novo email: ");
        String email = scanner.nextLine();
        System.out.print("Nova senha: ");
        String senha = scanner.nextLine();
        System.out.print("Novo endereço: ");
        String endereco = scanner.nextLine();

        // Atualiza os dados do comprador
        comprador.setNome(nome);
        comprador.setEmail(email);
        comprador.setSenha(senha);
        comprador.setEndereco(endereco);

        try {
            facade.atualizarComprador(comprador);
            System.out.println("Comprador atualizado com sucesso: " + comprador.getNome());
        } catch (Exception e) {
            System.out.println("Erro ao atualizar comprador: " + e.getMessage());
        }
    }

    private void removerComprador() {
        System.out.print("Digite o CPF do comprador: ");
        String cpf = scanner.nextLine();
        facade.removerComprador(cpf);
        System.out.println("Comprador removido com sucesso");
    }
}
