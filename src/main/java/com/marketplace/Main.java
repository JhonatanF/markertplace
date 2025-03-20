package com.marketplace;

import com.marketplace.facade.MarketplaceFacade;
import com.marketplace.menu.MainMenu;
import com.marketplace.repository.CompradorRepository;
import com.marketplace.repository.LojaRepository;
import com.marketplace.repository.ProdutoRepository;
import com.marketplace.service.CompradorService;
import com.marketplace.service.LojaService;
import com.marketplace.service.ProdutoService;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        MarketplaceFacade facade = inicializarSistema();
        MainMenu mainMenu = new MainMenu(scanner, facade);
        mainMenu.show();
        scanner.close();
    }

    private static MarketplaceFacade inicializarSistema() {
        LojaRepository lojaRepo = new LojaRepository();
        CompradorRepository compradorRepo = new CompradorRepository();
        ProdutoRepository produtoRepo = new ProdutoRepository();

        LojaService lojaService = new LojaService(lojaRepo);
        CompradorService compradorService = new CompradorService(compradorRepo);
        ProdutoService produtoService = new ProdutoService(produtoRepo, lojaRepo);

        return new MarketplaceFacade(lojaService, compradorService, produtoService);
    }
}
