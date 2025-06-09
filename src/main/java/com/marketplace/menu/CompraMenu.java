package com.marketplace.menu;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

import com.marketplace.facade.MarketplaceFacade;
import com.marketplace.model.Comprador;
import com.marketplace.model.Historico;
import com.marketplace.model.Loja;
import com.marketplace.model.Produto;

public class CompraMenu extends Menu {
    private final MarketplaceFacade facade;
    private Comprador comprador;
    private List<Produto> carrinho;

    protected CompraMenu(Scanner scanner, MarketplaceFacade facade, Comprador comprador, List<Produto> carrinho) {
        super(scanner, "MENU DE COMPRA");
        this.facade = facade;
        this.comprador = comprador;
        this.carrinho = carrinho;
    }

    @Override
    protected void initializeOptions() {
        addOption(1, new MenuOption() {
            public String getDescription() {
                return "Listar lojas";
            }

            public void execute() {
                listarLojas();
            }
        });

        addOption(2, new MenuOption() {
            public String getDescription() {
                return "Visualizar Produtos da Loja";
            }

            public void execute() {
                visualizarProdutos();
            }
        });

        addOption(3, new MenuOption() {
            public String getDescription() {
                return "Gerenciar Carrinho";
            }

            public void execute() {
                new CarrinhoMenu(scanner, facade, comprador, carrinho).show();
            }
        });

        addOption(4, new MenuOption() {
            public String getDescription() {
                return "Finalizar Compra";
            }

            public void execute() {
                finalizarCompra();
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

    public void listarLojas() {
        var lojas = facade.listarLojas();
        if (lojas.isEmpty()) {
            System.out.println("Nenhuma loja cadastrada");
            return;
        }
        System.out.println("\nLOJAS CADASTRADAS:");
        for (Loja loja : lojas) {
            System.out.println(loja.getNome() + " | CNPJ:" + loja.getCpfCnpj() + " | Contato:" + loja.getEmail()
                    + " | Conteito: " + facade.calcularConceitoLoja(loja.getCpfCnpj()) + " |");
        }
    }

    public void visualizarProdutos() {
        int option = 2;
        String cpfCnpj = "";
        List<Produto> produtos = null;

        do {
            if (option == 2) {
                System.out.print("Digite o Nome da loja:");
                String nome = scanner.nextLine();

                produtos = facade.listarProdutosDaLojaNome(nome);

                if (produtos.isEmpty()) {
                    System.out.println("Nenhum produto encontrado para esta loja");
                    return;
                } else {
                    System.out.println("OPÇÕES");
                    System.out.println("1. Comprar Produto");
                    System.out.println("0. Voltar");
                    System.out.print("Escolha uma opção: ");

                    option = scanner.nextInt();
                    scanner.nextLine();
                }
            }

            if (option == 1) {
                System.out.print("Digite o Nome do produto: ");
                String nomeProduto = scanner.nextLine();

                System.out.print("Digite a Quantidade: ");
                int qtdProduto = scanner.nextInt();
                scanner.nextLine();

                Optional<Produto> produto = facade.buscarProdutoPorNome(nomeProduto);

                if (produto.isPresent()) {
                    Produto prod = produto.get();

                    // atualizando quantidade do produto
                    prod = facade.atualizarProduto(prod.getId(), prod.getNome(), prod.getValor(), prod.getTipo(),
                            (prod.getQuantidade() - qtdProduto), prod.getMarca(), prod.getDescricao(), cpfCnpj);

                    Produto produtoComprado = (Produto) prod.clone();
                    produtoComprado.setQuantidade(qtdProduto);

                    if (carrinho == null) {
                        carrinho = new ArrayList<>();
                        carrinho.add(produtoComprado);
                    } else {
                        carrinho.add(produtoComprado);
                    }
                    System.out.println("Produto adicionado ao carrinho!\n");

                    System.out.println("OPÇÕES");
                    System.out.println("1. Comprar Outro Produto");
                    System.out.println("2. Selecionar outra Loja");
                    System.out.println("3. Gerenciar Carrinho");
                    System.out.println("0. Sair");
                    System.out.print("Escolha uma opção: ");
                    option = scanner.nextInt();
                    scanner.nextLine();

                    if (option == 1) {
                        exibirProdutos(produtos);
                    }

                } else {
                    System.out.println("Produto não existe");
                }
            }
            if (option == 3) {
                new CarrinhoMenu(scanner, facade, comprador, carrinho).show();
            }
        } while (option > 0 && option < 3);
    }

    private void exibirProdutos(List<Produto> produtos) {

        System.out.println("Produtos da Loja:");
        for (Produto produto : produtos) {
            System.out.println(produto.getNome() + " | valor:" + produto.getValor() + " | id:" + produto.getId());
        }
        System.out.println();
    }

    private void finalizarCompra() {
        if (!carrinho.isEmpty()) {
            System.out.println("\n== FINALIZANDO COMPRA ==");
            System.out.println("Produtos:");
            double total = 0;

            int pontuacao = comprador.getPontuacao();
            for (Produto produto : carrinho) {
                total += produto.getValor() * produto.getQuantidade();
                System.out.println(produto.getNome() + " | valor:" + produto.getValor() + " | quantidade:"
                        + produto.getQuantidade());
            }
            System.out.println("Total: " + total);

            // Novo desconto de 10% com 5 pontos
            if (pontuacao >= 12) {
                System.out.println("\nDeseja usar 12 pontos para garantir 10% de desconto? (S/N)");
                String respostaDescontoExtra = scanner.nextLine().trim();
                if (respostaDescontoExtra.equalsIgnoreCase("S")) {
                    double desconto10 = total * 0.10;
                    total -= desconto10;

                    System.out.println("12 Pontos utilizados. Desconto de 10% aplicado: R$" + desconto10);
                    System.out.println("Total com desconto: R$" + total);
                }
            } else {
                System.out.println(
                        "\nVocê não tem pontos suficientes para o desconto de 10%. \nProsseguindo com a compra...\n\n");
            }

            System.out.println("\nOPÇÕES");
            System.out.println("1. Confirmar compra");
            System.out.println("0. Voltar");
            System.out.print("Escolha uma opção: ");
            int option = scanner.nextInt();
            scanner.nextLine();

            if (option == 1) {
                if (pontuacao >= 12) {
                    facade.decrementarPontuacao(comprador.getCpf());
                }
                int gastoPara1Ponto = 50;
                int bonus_pontuacao = (int) ((total - total % gastoPara1Ponto) / gastoPara1Ponto);
                facade.adicionarPontuacao(bonus_pontuacao, comprador.getCpf());
                // Registrar a compra no histórico
                String compraId = facade.registrarCompra(comprador.getCpf(), carrinho, total);

                System.out.println(
                        "Compra Realizada com Sucesso! " + bonus_pontuacao + " pontos adicionados à sua pontuação");

                System.out.println("\nDeseja avaliar esta compra? (S/N)");
                String respostaAvaliacao = scanner.nextLine().trim();
                if (respostaAvaliacao.equalsIgnoreCase("S")) {
                    for (Produto produto : carrinho) {
                        System.out.println("\nProduto: " + produto.getNome());

                        int nota;
                        do {
                            System.out.print("Dê uma nota de 1 a 5 para este produto: ");
                            nota = scanner.nextInt();
                            scanner.nextLine(); // limpar buffer
                            if (nota < 1 || nota > 5) {
                                System.out.println("Nota inválida. Digite um valor entre 1 e 5.");
                            }
                        } while (nota < 1 || nota > 5);

                        System.out.print("Digite um comentário (opcional): ");
                        String comentario = scanner.nextLine();

                        facade.cadastrarAvaliacao(comprador.getCpf(), compraId, nota, comentario,
                                produto.getLojaCpfCnpj());
                        System.out.println("Avaliação registrada com sucesso para o produto: " + produto.getNome()
                                + "Pontuação adicionada (1 ponto).\n");
                        bonus_pontuacao = 1;
                        facade.adicionarPontuacao(bonus_pontuacao, comprador.getCpf());
                    }
                }
                carrinho.clear();

            }
        } else {
            System.out.println("\nO carrinho está vazio!");
        }
    }
}
