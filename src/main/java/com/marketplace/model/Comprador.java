package com.marketplace.model;

public class Comprador extends Usuario {
    private static final long serialVersionUID = 1L;
    
    private String cpf;
    private int pontuacao;

    public Comprador(String nome, String email, String senha, String cpf, String endereco) {
        super(nome, email, senha, endereco);
        this.cpf = cpf;
        this.pontuacao = 0;
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

    public void incrementarPontuacao(int valor) {
        this.pontuacao += valor;
    }

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
