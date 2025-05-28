package com.marketplace.model;

public class Avaliacao {
    private String id;
    private String compradorCpf; // Quem avaliou
    private String historicoId; // ID da compra no histórico
    private int nota; // Nota de 1 a 5
    private String comentario;
    private String lojaId;

    public Avaliacao() {
    }

    public Avaliacao(String compradorCpf, String historicoId, int nota, String comentario, String lojaId) {
        this.compradorCpf = compradorCpf;
        this.historicoId = historicoId;
        this.nota = nota;
        this.comentario = comentario;
        this.lojaId = lojaId;
    }

    // getters e setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setLojaId(String lojaId) {
        this.lojaId = lojaId;
    }

    public String getLojaId() {
        return this.lojaId;
    }

    public String getCompradorCpf() {
        return compradorCpf;
    }

    public void setCompradorCpf(String compradorCpf) {
        this.compradorCpf = compradorCpf;
    }

    public String getHistoricoId() {
        return historicoId;
    }

    public void setHistoricoId(String historicoId) {
        this.historicoId = historicoId;
    }

    public int getNota() {
        return nota;
    }

    public void setNota(int nota) {
        this.nota = nota;
    }

    public String getComentario() {
        return comentario;
    }

    public void setComentario(String comentario) {
        this.comentario = comentario;
    }
}