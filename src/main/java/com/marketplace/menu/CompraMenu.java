package com.marketplace.menu;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

import com.marketplace.facade.MarketplaceFacade;
import com.marketplace.model.Comprador;
import com.marketplace.model.Produto;

public class CompraMenu extends Menu{
    private final MarketplaceFacade facade;
    private Comprador comprador;
    private List<Produto> carrinho;


    protected CompraMenu(Scanner scanner, String title, MarketplaceFacade facade, Comprador comprador) {
        super(scanner, title);
        //TODO Auto-generated constructor stub
        this.facade = facade;
        this.comprador = comprador;
        this.carrinho = null;
    }

    @Override
    protected void initializeOptions() {
        addOption(1, new MenuOption() {
            public String getDescription() { return "Listar lojas"; }
            public void execute() { listarLojas(); }
        });

        addOption(2, new MenuOption() {
            public String getDescription() { return "Visualizar Produtos da Loja"; }
            public void execute() { visualizarProdutos(); }
        });

        addOption(3, new MenuOption() {
            public String getDescription() { return "Buscar Produto"; }
            public void execute() { buscarProduto(); }
        });

        addOption(4, new MenuOption() {
            public String getDescription() { return "Gerenciar Carrinho"; }
            public void execute() { visualizarCarrinho(); }
        });
        
        addOption(0, new MenuOption() {
            public String getDescription() { return "Sair"; }
            public void execute() { }
        });
    }

    public void listarLojas(){
        var lojas = facade.listarLojas();
        if (lojas.isEmpty()) {
            System.out.println("Nenhuma loja cadastrada");
            return;
        }
        lojas.forEach(System.out::println);
    }

    public void visualizarProdutos(){
        int option = 2;
        String cpfCnpj = "";
        List<Produto> produtos = null;
        
        do{
            if(option == 2){
                System.out.print("Digite o CPF/CNPJ da loja:");
                cpfCnpj = scanner.nextLine();

                produtos = facade.listarProdutosDaLoja(cpfCnpj);

                if (produtos.isEmpty()) {
                    System.out.println("Nenhum produto encontrado para esta loja");
                    return;
                }else{
                    produtos.forEach(System.out::println);
                    System.out.println("OPÇÕES");
                    System.out.println("1. Comprar Produto");
                    System.out.println("0. Voltar");
    
                    option = scanner.nextInt();
                    scanner.nextLine();
                }
            }

            if(option == 1){
                System.out.print("Digite o Id do produto:");
                String idProduto = scanner.nextLine();
                System.out.print("Digite a Quantidade: ");
                int qtdProduto = scanner.nextInt();
                scanner.nextLine();

                Optional<Produto> produto = facade.buscarProduto(idProduto);

                if (!produto.isEmpty()) {
                    Produto prod = produto.get();
                    prod = facade.atualizarProduto(prod.getId(),prod.getNome(),prod.getValor(),prod.getTipo(),
                            (prod.getQuantidade()-qtdProduto),prod.getMarca(),prod.getDescricao(),cpfCnpj);

                    Produto produtoComprado = (Produto) prod.clone();
                    produtoComprado.setQuantidade(qtdProduto);

                    if(carrinho == null){
                        carrinho = new ArrayList<>();
                        carrinho.add(produtoComprado);
                    }else{
                        carrinho.add(produtoComprado);
                    }
                    System.out.println("Compra Realizada com Sucesso");

                    System.out.println("OPÇÕES");
                    System.out.println("1. Comprar Outro Produto");
                    System.out.println("2. Selecionar outra Loja");
                    System.out.println("0. Sair");

                    if(option == 1){
                        produtos.forEach(System.out::println);
                    }
                }else{
                    System.out.println("Produto não existe");
                }
            }
        }while (option > 0 && option < 3);
    }

    public void buscarProduto(){
        //TODO
    }
    
    public void visualizarCarrinho(){
        if(carrinho == null){
            System.out.println("Não existem itens no carrinho");
        }else{
            carrinho.forEach(System.out::println);
        }
    }
}
