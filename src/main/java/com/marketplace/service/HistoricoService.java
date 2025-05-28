package com.marketplace.service;

import java.util.ArrayList;
import java.util.List;

import com.marketplace.model.Historico;
import com.marketplace.repository.HistoricoRepository;

public class HistoricoService {
    private final HistoricoRepository repository;
    private List<Historico> historicos;

    public HistoricoService(HistoricoRepository repository) {
        this.repository = repository;
        this.historicos = new ArrayList<>();

        Object[] data = repository.parseData();

        for (int i = 0; i < data.length; i++) {
            this.historicos.add((Historico) data[i]);
        }
    }

    public Historico cadastrar(Historico historico) {
        if (exists(historico.getCpf())) {
            throw new IllegalArgumentException("Historico Já cadastrado");
        }
        this.historicos.add(historico);
        repository.writeData(historicos.toArray());
        return historico;
    }

    public Historico buscarPorId(String id) {
        for (Historico hist : historicos) {
            if (id.equals(hist.getId()))
                return hist;
        }
        return null;
    }

    public List<Historico> buscarPorCpf(String cpf) {
        List<Historico> result = new ArrayList<>();
        for (Historico hist : historicos) {
            if (cpf.equals(hist.getCpf()))
                result.add(hist);
        }
        return result;
    }

    public List<Historico> listarTodos() {
        return historicos;
    }

    public Historico atualizar(Historico historico) {
        if (!exists(historico.getCpf())) {
            throw new IllegalArgumentException("Historico não encontrado");
        }
        for (Historico hist : historicos) {
            if (historico.getCpf().equals(hist.getCpf()))
                hist = historico;
        }

        repository.writeData(historicos.toArray());

        return historico;
    }

    public boolean exists(String cpf) {
        for (Historico hist : historicos) {
            if (cpf.equals(hist.getCpf()))
                return true;
        }
        return false;
    }
}
