package com.marketplace.menu;

import com.marketplace.facade.MarketplaceFacade;

import java.util.Scanner;

public class AdminLoginMenu extends Menu{
    private final MarketplaceFacade facade;

    public AdminLoginMenu(Scanner scanner, MarketplaceFacade facade) {
        super(scanner, "MARKETPLACE");
        this.facade = facade;
    }

    @Override
    protected void initializeOptions() {
        addOption(1, new MenuOption() {
            public String getDescription() { return "Gerenciar Lojas"; }
            public void execute() { new LojaMenu(scanner, facade).show(); }
        });

        addOption(2, new MenuOption() {
            public String getDescription() { return "Gerenciar Compradores"; }
            public void execute() { new CompradorMenu(scanner, facade).show(); }
        });

        addOption(3, new MenuOption() {
            public String getDescription() { return "Gerenciar Produtos"; }
            public void execute() { new ProdutoMenu(scanner, facade).show(); }
        });

        addOption(4, new MenuOption() {
            public String getDescription() { return "Gerenciar Administradores"; }
            public void execute() { new AdminMenu(scanner, facade).show(); }
        });

        addOption(0, new MenuOption() {
            public String getDescription() { return "Sair"; }
            public void execute() { }
        });
    }
}
