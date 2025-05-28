package com.marketplace.menu;

import com.marketplace.facade.MarketplaceFacade;
import com.marketplace.model.Comprador;
import com.marketplace.model.Historico;

import java.util.Optional;
import java.util.Scanner;

public class CompradorLoginMenu extends Menu {
    private final MarketplaceFacade facade;
    private Comprador comprador;

    protected CompradorLoginMenu(Scanner scanner, MarketplaceFacade facade, Comprador comprador) {
        super(scanner, "ÁREA DO COMPRADOR");
        this.facade = facade;
        this.comprador = comprador;
    }

    @Override
    protected void initializeOptions() {
        addOption(1, new MenuOption() {
            public String getDescription() {
                return "Navegar Pelo Sistema";
            }

            public void execute() {
                new CompraMenu(scanner, facade, comprador, null).show();
            }
        });

        addOption(2, new MenuOption() {
            public String getDescription() {
                return "Atualizar Dados";
            }

            public void execute() {
                atualizarComprador();
            }
        });

        addOption(3, new MenuOption() {
            public String getDescription() {
                return "Visualizar Historico";
            }

            public void execute() {
                visualizarHistorico();
            }
        });

        addOption(0, new MenuOption() {
            public String getDescription() {
                return "Sair";
            }

            public void execute() {
            }
        });
    }

    private void atualizarComprador() {
        String cpf = comprador.getCpf();
        if (facade.buscarComprador(cpf).isEmpty()) {
            System.out.println("Comprador não encontrado");
            return;
        }

        String nome = comprador.getNome();
        String email = comprador.getEmail();
        String senha = comprador.getSenha();
        String endereco = comprador.getEndereco();

        int option;

        do {
            System.out.println("Escolha o dado a ser alterado:");
            System.out.println("1. Nome: " + nome);
            System.out.println("2. Email: " + email);
            System.out.println("3. Senha: " + senha);
            System.out.println("4. Endereco: " + endereco);
            System.out.println("0. Cancelar");

            option = scanner.nextInt();
            scanner.nextLine(); // Consume newline
        } while (option < 1 || option > 4);

        System.out.println("Novo dado:");
        String novoDado = scanner.nextLine();

        if (option > 0) {
            if (option == 1) {
                nome = novoDado;
            } else if (option == 2) {
                email = novoDado;
            } else if (option == 3) {
                senha = novoDado;
            } else if (option == 4) {
                endereco = novoDado;
            }
            this.comprador = facade.atualizarComprador(nome, email, senha, cpf, endereco);
            System.out.println("Comprador atualizado com sucesso: " + comprador.getNome());
        }
    }

    private void visualizarHistorico() {
        Historico historico = facade.buscarHistoricoComprador(comprador.getCpf());

        historico.printHistorico();
    }
}
