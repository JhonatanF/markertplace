package com.marketplace.model;

import java.util.List;
import java.util.ArrayList;

public class Historico {
    private String cpf;
    private List<Produto> historico;

    public Historico(String cpf, List<Produto> hist){
        this.cpf = cpf;
        this.historico = hist;
    }

    public Historico(String cpf){
        this.cpf = cpf;
        this.historico = new ArrayList<>();
    }

    public void addProdutos(List<Produto> newProd){
        for(Produto prod : newProd){
            this.historico.add(prod);
        }
    }

    public void printHistorico(){
        for(Produto prod : this.historico){
            System.out.println(prod);;
        }
    }

    public String getCpf(){
        return this.cpf;
    }

    public List<Produto> getHistorico(){
        return this.historico;
    }
}