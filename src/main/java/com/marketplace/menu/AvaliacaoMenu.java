package com.marketplace.menu;

import java.util.Scanner;

import com.marketplace.facade.MarketplaceFacade;

public class AvaliacaoMenu extends Menu {
    private final MarketplaceFacade facade;
    private final String compradorCpf;

    public AvaliacaoMenu(Scanner scanner, MarketplaceFacade facade, String compradorCpf) {
        super(scanner, "Menu de Avaliação");
        this.facade = facade;
        this.compradorCpf = compradorCpf;
    }

    @Override
    protected void initializeOptions() {
        addOption(1, new MenuOption() {
            public String getDescription() {
                return "Avaliar Compra";
            }

            public void execute() {
                avaliarCompra();
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

    private void avaliarCompra() {
        System.out.print("Digite o ID da Compra (Histórico): ");
        String historicoId = scanner.nextLine();

        System.out.print("Digite a nota (1 a 5): ");
        int nota = Integer.parseInt(scanner.nextLine());

        System.out.print("Digite seu comentário: ");
        String comentario = scanner.nextLine();

        try {
            facade.cadastrarAvaliacao(compradorCpf, historicoId, nota, comentario);
            System.out.println("Avaliação cadastrada com sucesso!");
        } catch (IllegalArgumentException e) {
            System.out.println("Erro na avaliação: " + e.getMessage());
        }
    }
}