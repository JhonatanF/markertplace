package com.marketplace.service;

import com.marketplace.model.Historico;
import com.marketplace.repository.HistoricoRepository;

import java.util.List;
import java.util.Optional;

public class HistoricoService {
    private final HistoricoRepository repository;

    public HistoricoService(HistoricoRepository repository) {
        this.repository = repository;
    }

    public Historico cadastrarOuAtualizarHistorico(String compradorCpf, List<Historico.Compra> compras) {
        // Busca o histórico existente ou cria um novo se não existir
        Historico historico = buscarPorComprador(compradorCpf);

        // Adiciona todas as novas compras ao histórico
        historico.getCompras().addAll(compras);

        // Salva e retorna o histórico atualizado
        return repository.salvar(historico);
    }

    public Historico buscarPorComprador(String compradorCpf) {
        Optional<Historico> historicoOpt = repository.buscarPorComprador(compradorCpf);

        if (historicoOpt.isPresent()) {
            return historicoOpt.get(); // Retorna o histórico existente
        } else {
            // Cria um novo histórico se não existir
            Historico novoHistorico = new Historico(compradorCpf);
            return repository.salvar(novoHistorico);
        }
    }

    public List<Historico> listarTodos() {
        return repository.listarTodos();
    }

    public void removerHistoricos() {
        repository.removerHistoricos();
    }
}
