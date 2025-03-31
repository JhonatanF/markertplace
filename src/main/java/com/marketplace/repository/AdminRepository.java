package com.marketplace.repository;

import com.marketplace.model.Admin;

public class AdminRepository extends AbstractFileRepository<Admin, String> {
    public AdminRepository() {
        super("admins.json");
    }

    @Override
    protected String getId(Admin entity) {
        return entity.getCpf();
    }
}