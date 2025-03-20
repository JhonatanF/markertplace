package com.marketplace.menu;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Scanner;

public abstract class Menu {
    protected final Scanner scanner;
    protected final Map<Integer, MenuOption> options;
    protected final String title;

    protected Menu(Scanner scanner, String title) {
        this.scanner = scanner;
        this.title = title;
        this.options = new LinkedHashMap<>();
        initializeOptions();
    }

    protected abstract void initializeOptions();

    public void show() {
        while (true) {
            System.out.println("\n=== " + title + " ===");
            options.forEach((key, option) -> 
                System.out.println(key + ". " + option.getDescription()));
            System.out.print("Escolha uma opção: ");

            try {
                int choice = scanner.nextInt();
                scanner.nextLine(); // Consume newline

                if (choice == 0) {
                    break;
                }

                MenuOption option = options.get(choice);
                if (option != null) {
                    try {
                        option.execute();
                    } catch (Exception e) {
                        System.out.println("Erro: " + e.getMessage());
                    }
                } else {
                    System.out.println("Opção inválida!");
                }
            } catch (Exception e) {
                System.out.println("Entrada inválida. Por favor, digite um número.");
                scanner.nextLine(); // Clear the invalid input
            }
        }
    }

    protected void addOption(int key, MenuOption option) {
        options.put(key, option);
    }
}
