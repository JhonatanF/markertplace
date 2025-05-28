package com.marketplace.service;

import com.marketplace.model.Loja;
import com.marketplace.repository.LojaRepository;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class LojaService {
    private final LojaRepository repository;

    public LojaService(LojaRepository repository) {
        this.repository = repository;
    }

    public Loja cadastrar(Loja loja) {
        if (repository.exists(loja.getCpfCnpj())) {
            throw new IllegalArgumentException("Loja já cadastrada com este CPF/CNPJ");
        }
        return repository.save(loja);
    }

    public Optional<Loja> buscarPorId(String cpfCnpj) {
        return repository.findById(cpfCnpj);
    }

    public List<Loja> buscarPorNome(String nome) {
        return repository.findAll()
                .stream()
                .filter(loja -> loja.getNome().toLowerCase().contains(nome.toLowerCase()))
                .collect(Collectors.toList());
    }

    public List<Loja> listarTodas() {
        return repository.findAll();
    }

    public Loja atualizar(Loja loja) {
        if (!repository.exists(loja.getCpfCnpj())) {
            throw new IllegalArgumentException("Loja não encontrada");
        }
        return repository.save(loja);
    }

    public void remover(String cpfCnpj) {
        if (!repository.exists(cpfCnpj)) {
            throw new IllegalArgumentException("Loja não encontrada");
        }
        repository.delete(cpfCnpj);
    }

    public void removerLojas () {
        for (Loja loja : repository.findAll()) {
            remover(loja.getCpfCnpj());
        }
    }
}
