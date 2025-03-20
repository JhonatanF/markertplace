package com.marketplace.repository;

import com.marketplace.model.Comprador;

public class CompradorRepository extends AbstractFileRepository<Comprador, String> {
    public CompradorRepository() {
        super("compradores.json");
    }

    @Override
    protected String getId(Comprador entity) {
        return entity.getCpf();
    }
}
