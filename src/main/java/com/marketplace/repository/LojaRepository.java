package com.marketplace.repository;

import com.marketplace.model.Loja;

public class LojaRepository extends AbstractFileRepository<Loja, String> {
    public LojaRepository() {
        super("lojas.json");
    }

    @Override
    protected String getId(Loja entity) {
        return entity.getCpfCnpj();
    }
}
