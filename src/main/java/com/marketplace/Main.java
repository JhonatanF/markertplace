package com.marketplace;

import com.marketplace.facade.MarketplaceFacade;
import com.marketplace.menu.MainMenu;
import com.marketplace.model.Historico;
import com.marketplace.repository.AdminRepository;
import com.marketplace.repository.AvaliacaoRepository;
import com.marketplace.repository.CompradorRepository;
import com.marketplace.repository.LojaRepository;
import com.marketplace.repository.ProdutoRepository;
import com.marketplace.repository.HistoricoRepository;
import com.marketplace.service.AdminService;
import com.marketplace.service.AvaliacaoService;
import com.marketplace.service.CompradorService;
import com.marketplace.service.LojaService;
import com.marketplace.service.ProdutoService;
import com.marketplace.service.HistoricoService;

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
        AdminRepository adminRepo = new AdminRepository();
        HistoricoRepository historicoRepo = new HistoricoRepository(Historico.class);

        // Adicione aqui AvaliacaoRepository e AvaliacaoService
        AvaliacaoRepository avaliacaoRepo = new AvaliacaoRepository();
        AvaliacaoService avaliacaoService = new AvaliacaoService(avaliacaoRepo);

        LojaService lojaService = new LojaService(lojaRepo);
        CompradorService compradorService = new CompradorService(compradorRepo);
        ProdutoService produtoService = new ProdutoService(produtoRepo, lojaRepo);
        AdminService adminService = new AdminService(adminRepo);
        HistoricoService historicoService = new HistoricoService(historicoRepo);

        // Agora, passe o avaliacaoService para a fachada (MarketplaceFacade)
        return new MarketplaceFacade(lojaService, compradorService, produtoService, adminService, historicoService,
                avaliacaoService);
    }

}
