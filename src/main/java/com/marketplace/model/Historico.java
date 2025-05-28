package com.marketplace.model;

import java.util.List;
import java.util.ArrayList;
import java.util.UUID;

public class Historico {
    private String id;
    private String cpf;
    private List<Produto> historico;

    public Historico(String cpf, List<Produto> hist) {
        this.id = UUID.randomUUID().toString();
        this.cpf = cpf;
        this.historico = hist;
    }

    public Historico(String cpf) {
        this.id = UUID.randomUUID().toString();
        this.cpf = cpf;
        this.historico = new ArrayList<>();
    }

    public void addProdutos(List<Produto> newProd) {
        for (Produto prod : newProd) {
            this.historico.add(prod);
        }
    }

    public void printHistorico() {
        for (Produto prod : this.historico) {
            System.out.println(prod);
            ;
        }
    }

    public String getCpf() {
        return this.cpf;
    }

    public String getId() {
        return id;
    }

    public List<Produto> getHistorico() {
        return this.historico;
    }
}