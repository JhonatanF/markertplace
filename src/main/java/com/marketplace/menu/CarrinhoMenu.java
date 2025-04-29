package com.marketplace.menu;

import com.marketplace.facade.MarketplaceFacade;
import com.marketplace.model.Comprador;
import com.marketplace.model.Produto;
import java.util.List;
import java.util.Scanner;

public class CarrinhoMenu extends Menu {

    MarketplaceFacade facade;
    List<Produto> carrinho;
    Comprador comprador;

    protected CarrinhoMenu(Scanner scanner, MarketplaceFacade facade, Comprador comprador, List<Produto> carrinho) {
        super(scanner, "MENU DO CARRINHO");
        this.comprador = comprador;
        this.facade = facade;
        this.carrinho = carrinho;
    }

    @Override
    protected void initializeOptions() {
        addOption(1, new MenuOption() {
            public String getDescription() { return "Visualizar Itens";}
            public void execute() { visualizarCarrinho();}
        });

        addOption(2,new MenuOption() {
            public String getDescription() { return "Adicionar mais itens";}
            public void execute() {new CompraMenu(scanner,facade,comprador,carrinho).show();}
        });

        addOption(3,new MenuOption() {
            public String getDescription() { return "Remover item";}
            public void execute() { removerItemDoCarrinho();}
        });

        addOption(0,new MenuOption() {
            public String getDescription() { return "Voltar";}
            public void execute() { }
        });
    }

    private void removerItemDoCarrinho() {

        System.out.print("Digite o nome do item:");
        String nome = scanner.nextLine();
        int indexToRemove = -1;
        boolean firstItem = true;

        for (int i = 0; i < carrinho.size(); i++) {
            if (carrinho.get(i).getNome().equals(nome)) {
                if (firstItem) {
                    indexToRemove = i;
                    System.out.println("Item removido com sucesso!");
                    firstItem = false;
                }
            }
        }
        if (indexToRemove != -1) carrinho.remove(indexToRemove);
        else {
            System.out.println("O nome digitado não corresponde a nenhum item");
        }
    }

    private void visualizarCarrinho(){
        if(carrinho == null || carrinho.isEmpty()) {
            System.out.println("\nNão existem itens no carrinho");
        }else{
            System.out.println("\nITENS NO CARRINHO:");
            for (Produto produto : carrinho) {
                System.out.println(produto.getNome()+" | valor:"+produto.getValor()+" | quantidade:"+produto.getQuantidade());
            }
        }
    }
}
