package com.marketplace.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public class Historico {
    private String compradorCpf;
    private List<Compra> compras;

    public Historico(String compradorCpf) {
        this.compradorCpf = compradorCpf;
        this.compras = new ArrayList<>();
    }

    public void adicionarCompra(List<Produto> produtos, double total) {
        Compra compra = new Compra(produtos, total);
        this.compras.add(compra);
    }

    public String getCompradorCpf() {
        return compradorCpf;
    }

    public List<Compra> getCompras() {
        return compras;
    }

    public static class Compra {
        private String id;
        private Date data;
        private double total;
        private List<Produto> produtos;

        public Compra(List<Produto> produtos, double total) {
            this.id = UUID.randomUUID().toString();
            this.data = new Date();
            this.total = total;
            this.produtos = new ArrayList<>(produtos);
        }

        public String getId() {
            return id;
        }

        public Date getData() {
            return data;
        }

        public double getTotal() {
            return total;
        }

        public List<Produto> getProdutos() {
            return produtos;
        }
    }

    public void printHistorico() {
        System.out.println("=== Histórico de Compras ===");
        System.out.println("Comprador CPF: " + compradorCpf);
        System.out.println("Total de compras: " + compras.size());

        if (compras.isEmpty()) {
            System.out.println("Nenhuma compra realizada.");
            return;
        }

        for (int i = 0; i < compras.size(); i++) {
            Compra compra = compras.get(i);
            System.out.println("\nCompra #" + (i + 1));
            System.out.println("ID: " + compra.getId());
            System.out.println("Data: " + compra.getData());
            System.out.printf("Total: R$ %.2f\n", compra.getTotal());
            System.out.println("Produtos:");

            List<Produto> produtos = compra.getProdutos();
            for (int j = 0; j < produtos.size(); j++) {
                Produto p = produtos.get(j);
                System.out.printf("  - %s | Código: %s | Preço: R$ %.2f\n", p.getNome(), p.getId(), p.getValor());
            }
        }

        System.out.println("\n=== Fim do Histórico ===");
    }

}