package com.marketplace.service;

import com.marketplace.model.Produto;
import com.marketplace.repository.ProdutoRepository;
import com.marketplace.repository.LojaRepository;
import java.util.List;
import java.util.Optional;

public class ProdutoService {
    private final ProdutoRepository repository;
    private final LojaRepository lojaRepository;

    public ProdutoService(ProdutoRepository repository, LojaRepository lojaRepository) {
        this.repository = repository;
        this.lojaRepository = lojaRepository;
    }

    public Produto cadastrar(Produto produto) {
        if (produto.getQuantidade() < 0) {
            throw new IllegalArgumentException("Quantidade não pode ser negativa");
        }
        if (produto.getValor() <= 0) {
            throw new IllegalArgumentException("Valor deve ser maior que zero");
        }
        if (!lojaRepository.exists(produto.getLojaCpfCnpj())) {
            throw new IllegalArgumentException("Loja não encontrada com o CPF/CNPJ: " + produto.getLojaCpfCnpj());
        }
        return repository.save(produto);
    }

    public Optional<Produto> buscarPorId(String id) {
        return repository.findById(id);
    }

    public List<Produto> listarTodos() {
        return repository.findAll();
    }

    public List<Produto> listarPorLoja(String lojaCpfCnpj) {
        return repository.findByLoja(lojaCpfCnpj);
    }

    public Produto atualizar(Produto produto) {
        if (!repository.exists(produto.getId())) {
            throw new IllegalArgumentException("Produto não encontrado");
        }
        if (produto.getQuantidade() < 0) {
            throw new IllegalArgumentException("Quantidade não pode ser negativa");
        }
        if (produto.getValor() <= 0) {
            throw new IllegalArgumentException("Valor deve ser maior que zero");
        }
        return repository.save(produto);
    }

    public void remover(String id) {
        if (!repository.exists(id)) {
            throw new IllegalArgumentException("Produto não encontrado");
        }
        repository.delete(id);
    }

    public void removerProdutos() {
        for (Produto produto : repository.findAll()) {
            remover(produto.getId());
        }
    }
}
