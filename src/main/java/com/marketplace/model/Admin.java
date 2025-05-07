package com.marketplace.model;

public class Admin extends Usuario {
    private static final long serialVersionUID = 1L;

    private String cpf;

    public Admin(String nome, String email, String senha, String cpf, String endereco) {
        super(nome, email, senha, endereco);
        this.cpf = cpf;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    @Override
    public String toString() {
        return "Administrador{" +
                "nome='" + nome + '\'' +
                ", email='" + email + '\'' +
                ", cpf='" + cpf + '\'' +
                ", endereco='" + endereco + '\'' +
                '}';
    }
}
