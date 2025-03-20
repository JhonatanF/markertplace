package com.marketplace.repository;

import com.marketplace.model.Produto;
import java.util.List;
import java.util.stream.Collectors;

public class ProdutoRepository extends AbstractFileRepository<Produto, String> {
    public ProdutoRepository() {
        super("produtos.json");
    }

    @Override
    protected String getId(Produto entity) {
        return entity.getId();
    }

    public List<Produto> findByLoja(String lojaCpfCnpj) {
        return findAll().stream()
                .filter(produto -> produto.getLojaCpfCnpj().equals(lojaCpfCnpj))
                .collect(Collectors.toList());
    }
}
