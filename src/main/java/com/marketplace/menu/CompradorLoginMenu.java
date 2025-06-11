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

        Optional<Comprador> compradorOptional = facade.buscarComprador(cpf);
        if (compradorOptional.isEmpty()) {
            System.out.println("Comprador não encontrado");
            return;
        }

        Comprador compradorAtual = compradorOptional.get();

        String nome = compradorAtual.getNome();
        String email = compradorAtual.getEmail();
        String senha = compradorAtual.getSenha();
        String endereco = compradorAtual.getEndereco();

        int option;

        do {
            System.out.println("Escolha o dado a ser alterado:");
            System.out.println("1. Nome: " + nome);
            System.out.println("2. Email: " + email);
            System.out.println("3. Senha: " + senha);
            System.out.println("4. Endereco: " + endereco);
            System.out.println("0. Cancelar");

            try {
                option = Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                option = -1;
            }
        } while (option < 0 || option > 4);

        if (option == 0) {
            return;
        }

        System.out.println("Novo dado:");
        String novoDado = scanner.nextLine();

        if (option == 1) {
            compradorAtual.setNome(novoDado);
        } else if (option == 2) {
            compradorAtual.setEmail(novoDado);
        } else if (option == 3) {
            compradorAtual.setSenha(novoDado);
        } else if (option == 4) {
            compradorAtual.setEndereco(novoDado);
        }

        try {
            facade.atualizarComprador(compradorAtual);
            this.comprador = compradorAtual; // Atualiza o campo local, se necessário
            System.out.println("Comprador atualizado com sucesso: " + comprador.getNome());
        } catch (Exception e) {
            System.out.println("Erro ao atualizar comprador: " + e.getMessage());
        }
    }

    private void visualizarHistorico() {
        Historico historico = facade.buscarHistoricoComprador(comprador.getCpf());

        historico.printHistorico();
    }

    private void visualizarPontos(){

    }
}
