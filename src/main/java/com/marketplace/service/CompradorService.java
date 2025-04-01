package com.marketplace.service;

import com.marketplace.model.Comprador;
import com.marketplace.repository.CompradorRepository;
import java.util.List;
import java.util.Optional;

public class CompradorService {
    private final CompradorRepository repository;

    public CompradorService(CompradorRepository repository) {
        this.repository = repository;
    }

    public Comprador cadastrar(Comprador comprador) {
        if (repository.exists(comprador.getCpf())) {
            throw new IllegalArgumentException("Comprador já cadastrado com este CPF");
        }
        return repository.save(comprador);
    }

    public Optional<Comprador> buscarPorId(String cpf) {
        return repository.findById(cpf);
    }

    public List<Comprador> listarTodos() {
        return repository.findAll();
    }

    public Comprador atualizar(Comprador comprador) {
        if (!repository.exists(comprador.getCpf())) {
            throw new IllegalArgumentException("Comprador não encontrado");
        }
        return repository.save(comprador);
    }

    public void remover(String cpf) {
        if (!repository.exists(cpf)) {
            throw new IllegalArgumentException("Comprador não encontrado");
        }
        repository.delete(cpf);
    }

    public void removerCompradores() {
        for (Comprador comprador : repository.findAll()) {
            remover(comprador.getCpf());
        }
    }
}
