package com.marketplace.model;

public class Loja extends Usuario {
    private static final long serialVersionUID = 1L;
    
    private String cpfCnpj;

    public Loja(String nome, String email, String senha, String cpfCnpj, String endereco) {
        super(nome, email, senha, endereco);
        this.cpfCnpj = cpfCnpj;
    }

    public String getCpfCnpj() {
        return cpfCnpj;
    }

    public void setCpfCnpj(String cpfCnpj) {
        this.cpfCnpj = cpfCnpj;
    }

    @Override
    public String toString() {
        return "Loja{" +
                "nome='" + nome + '\'' +
                ", email='" + email + '\'' +
                ", cpfCnpj='" + cpfCnpj + '\'' +
                ", endereco='" + endereco + '\'' +
                '}';
    }
}
