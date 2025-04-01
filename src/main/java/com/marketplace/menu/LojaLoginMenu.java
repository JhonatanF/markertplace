package com.marketplace.menu;

import com.marketplace.facade.MarketplaceFacade;
import com.marketplace.model.Loja;
import com.marketplace.model.Produto;

import java.util.Scanner;

public class LojaLoginMenu extends Menu{
    private final MarketplaceFacade facade;
    private Loja loja;

    protected LojaLoginMenu(Scanner scanner, MarketplaceFacade facade, Loja loja) {
        super(scanner, "ÁREA DA LOJA");
        this.facade = facade;
        this.loja = loja;
    }

    protected LojaLoginMenu(Scanner scanner, MarketplaceFacade facade) {
        super(scanner, "ÁREA DA LOJA");
        this.facade = facade;
    }

    @Override
    protected void initializeOptions() {
        addOption(1, new MenuOption() {
            public String getDescription() { return "Produtos"; }
            public void execute() { new ProdutoMenu(scanner, facade, loja).show(); }
        });

        addOption(2, new MenuOption() {
            public String getDescription() { return "Atualizar Dados"; }
            public void execute() { atualizarLoja(); }
        });

        addOption(0, new MenuOption() {
            public String getDescription() { return "Sair"; }
            public void execute() { }
        });
    }

    private void atualizarLoja() {
        String cpfCnpj = loja.getCpfCnpj();
        if (facade.buscarLoja(cpfCnpj).isEmpty()) {
            System.out.println("Loja não encontrada");
            return;
        }

        String nome = loja.getNome();
        String email = loja.getEmail();
        String senha = loja.getSenha();
        String endereco = loja.getEndereco();

        int option;

        do{
            System.out.println("Escolha o dado a ser alterado:");
            System.out.println("1. Nome: " + nome);
            System.out.println("2. Email: " + email);
            System.out.println("3. Senha: " + senha);
            System.out.println("4. Endereco: " + endereco);
            System.out.println("0. Cancelar");

            option = scanner.nextInt();
            scanner.nextLine(); // Consume newline
        }while(option < 0 || option > 4);

        System.out.println("Novo dado:");
        String novoDado = scanner.nextLine();

        if(option > 0){
            if(option == 1){
                nome = novoDado;
            } else if (option == 2) {
                email = novoDado;
            } else if (option == 3) {
                senha = novoDado;
            } else if (option == 4) {
                endereco = novoDado;
            }
            Loja loja = facade.atualizarLoja(nome, email, senha, cpfCnpj, endereco);
            System.out.println("Loja atualizada com sucesso: " + loja.getNome());
        }
    }
}
