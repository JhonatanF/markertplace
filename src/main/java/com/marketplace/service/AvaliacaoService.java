package com.marketplace.service;

import com.marketplace.model.Avaliacao;
import com.marketplace.repository.AvaliacaoRepository;

import java.util.List;

public class AvaliacaoService {
    private final AvaliacaoRepository repository;

    public AvaliacaoService(AvaliacaoRepository repository) {
        this.repository = repository;
    }

    public Avaliacao cadastrar(Avaliacao avaliacao) {
        if (avaliacao.getNota() < 1 || avaliacao.getNota() > 5) {
            throw new IllegalArgumentException("Nota deve ser entre 1 e 5.");
        }
        return repository.salvar(avaliacao);
    }

    public List<Avaliacao> listarPorCompra(String historicoId) {
        return repository.listarPorCompra(historicoId);
    }

    public String calcularMediaAvaliacoesLoja(String lojaId) {
        double media = repository.calcularMediaAvaliacoesPorLoja(lojaId);

        // Arredonda para 1 casa decimal
        double mediaArredondada = Math.round(media * 10) / 10.0;

        String classificacao;
        if (media <= 2) {
            classificacao = "Ruim";
        } else if (media <= 3) {
            classificacao = "Média";
        } else if (media <= 4) {
            classificacao = "Boa";
        } else {
            classificacao = "Ótima";
        }

        return String.format("%.1f - %s", mediaArredondada, classificacao);
    }
}