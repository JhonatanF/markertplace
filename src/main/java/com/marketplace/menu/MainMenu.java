package com.marketplace.menu;

import com.marketplace.facade.MarketplaceFacade;
import java.util.Scanner;

public class MainMenu extends Menu {
    private final MarketplaceFacade facade;

    public MainMenu(Scanner scanner, MarketplaceFacade facade) {
        super(scanner, "MARKETPLACE");
        this.facade = facade;
    }

    @Override
    protected void initializeOptions() {
        addOption(1, new MenuOption() {
            public String getDescription() { return "Login"; }
            public void execute() { new LoginMenu(scanner, facade).show(); }
        });

        addOption(2, new MenuOption() {
            public String getDescription() { return "Cadastro"; }
            public void execute() { new CadastroMenu(scanner, facade).show(); }
        });

        addOption(0, new MenuOption() {
            public String getDescription() { return "Sair"; }
            public void execute() { }
        });
    }
}
