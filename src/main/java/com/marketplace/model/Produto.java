package com.marketplace.model;

import java.io.Serializable;

public class Produto implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private String id;
    private String nome;
    private double valor;
    private String tipo;
    private int quantidade;
    private String marca;
    private String descricao;
    private String lojaCpfCnpj;

    public Produto(String nome, double valor, String tipo, int quantidade, String marca, String descricao, String lojaCpfCnpj) {
        this.id = java.util.UUID.randomUUID().toString();
        this.nome = nome;
        this.valor = valor;
        this.tipo = tipo;
        this.quantidade = quantidade;
        this.marca = marca;
        this.descricao = descricao;
        this.lojaCpfCnpj = lojaCpfCnpj;
    }

    public Produto(String id, String nome, double valor, String tipo, int quantidade, String marca, String descricao, String lojaCpfCnpj) {
        this.id = id;
        this.nome = nome;
        this.valor = valor;
        this.tipo = tipo;
        this.quantidade = quantidade;
        this.marca = marca;
        this.descricao = descricao;
        this.lojaCpfCnpj = lojaCpfCnpj;
    }

    // Getters and Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public double getValor() {
        return valor;
    }

    public void setValor(double valor) {
        this.valor = valor;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public int getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(int quantidade) {
        this.quantidade = quantidade;
    }

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getLojaCpfCnpj() {
        return lojaCpfCnpj;
    }

    public void setLojaCpfCnpj(String lojaCpfCnpj) {
        this.lojaCpfCnpj = lojaCpfCnpj;
    }

    @Override
    public String toString() {
        return "Produto{" +
                "id='" + id + '\'' +
                ", nome='" + nome + '\'' +
                ", valor=" + valor +
                ", tipo='" + tipo + '\'' +
                ", quantidade=" + quantidade +
                ", marca='" + marca + '\'' +
                ", descricao='" + descricao + '\'' +
                '}';
    }
}
