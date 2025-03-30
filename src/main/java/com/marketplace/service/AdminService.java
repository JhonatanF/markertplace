package com.marketplace.service;

import com.marketplace.model.Admin;
import com.marketplace.repository.AdminRepository;

import java.util.List;
import java.util.Optional;

public class AdminService {
    private final AdminRepository repository;

    public AdminService(AdminRepository adminRepository) {
        this.repository = adminRepository;
    }

    public Admin cadastrar(Admin admin) {
        if (repository.exists(admin.getCpf())) {
            throw new IllegalArgumentException("Administrador já cadastrado com este CPF");
        }
        return repository.save(admin);
    }

    public Optional<Admin> buscarPorId(String cpf) {
        return repository.findById(cpf);
    }

    public List<Admin> listarTodos() {
        return repository.findAll();
    }

    public Admin atualizar(Admin admin) {
        if (!repository.exists(admin.getCpf())) {
            throw new IllegalArgumentException("Administrador não encontrado");
        }
        return repository.save(admin);
    }

    public void remover(String cpf) {
        if (!repository.exists(cpf)) {
            throw new IllegalArgumentException("Administrador não encontrado");
        }
        repository.delete(cpf);
    }
}
