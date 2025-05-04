package com.marketplace.model;

import java.util.List;

public class Comprador extends Usuario {
    private static final long serialVersionUID = 1L;
    
    private String cpf;
    private int pontuacao;
    private List<Produto> carrinho;

    public Comprador(String nome, String email, String senha, String cpf, String endereco, List<Produto> carrinho) {
        super(nome, email, senha, endereco);
        this.cpf = cpf;
        this.pontuacao = 0;
        this.carrinho = carrinho;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public int getPontuacao() {
        return pontuacao;
    }

    public void setPontuacao(int pontuacao) {
        this.pontuacao = pontuacao;
    }

    public List<Produto> getCarrinho() {return this.carrinho;}

    public void setCarrinho(List<Produto> carrinho) {this.carrinho = carrinho;}

    @Override
    public String toString() {
        return "Comprador{" +
                "nome='" + nome + '\'' +
                ", email='" + email + '\'' +
                ", cpf='" + cpf + '\'' +
                ", endereco='" + endereco + '\'' +
                ", pontuacao=" + pontuacao +
                '}';
    }
}
