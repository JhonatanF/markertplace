package com.marketplace.repository;

import com.google.gson.*;
import org.apache.commons.io.IOUtils;

import com.marketplace.model.Avaliacao;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class AvaliacaoRepository {
    private final String path = "avaliacoes-v1.json";
    private final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    // Lista interna para manipulação
    private List<Avaliacao> avaliacoes = new ArrayList<>();

    public AvaliacaoRepository() {
        loadFromFile();
    }

    private void loadFromFile() {
        try (FileReader reader = new FileReader(path)) {
            String content = IOUtils.toString(reader);
            if (content.isBlank()) {
                avaliacoes = new ArrayList<>();
                return;
            }
            JsonObject jsonObject = gson.fromJson(content, JsonObject.class);
            JsonArray jsonArray = jsonObject.getAsJsonArray("avaliacoes");
            avaliacoes = new ArrayList<>();
            for (JsonElement elem : jsonArray) {
                Avaliacao a = gson.fromJson(elem, Avaliacao.class);
                avaliacoes.add(a);
            }
        } catch (FileNotFoundException e) {
            // Arquivo não existe: cria lista vazia e arquivo será criado ao salvar
            avaliacoes = new ArrayList<>();
        } catch (IOException e) {
            throw new RuntimeException("Erro ao ler arquivo de avaliações", e);
        }
    }

    public double calcularMediaAvaliacoesPorLoja(String lojaId) {
        return avaliacoes.stream()
                .filter(a -> a.getLojaId() != null && a.getLojaId().equals(lojaId))
                .mapToInt(Avaliacao::getNota)
                .average()
                .orElse(0.0);
    }

    private void saveToFile() {
        JsonArray jsonArray = new JsonArray();
        for (Avaliacao a : avaliacoes) {
            jsonArray.add(gson.toJsonTree(a));
        }
        JsonObject jsonObject = new JsonObject();
        jsonObject.add("avaliacoes", jsonArray);

        try (Writer writer = new FileWriter(path)) {
            gson.toJson(jsonObject, writer);
        } catch (IOException e) {
            throw new RuntimeException("Erro ao salvar arquivo de avaliações", e);
        }
    }

    public Avaliacao salvar(Avaliacao avaliacao) {
        if (avaliacao.getId() == null) {
            avaliacao.setId(UUID.randomUUID().toString());
        }
        avaliacoes.add(avaliacao);
        saveToFile();
        return avaliacao;
    }

    public List<Avaliacao> listarPorCompra(String historicoId) {
        List<Avaliacao> resultado = new ArrayList<>();
        for (Avaliacao a : avaliacoes) {
            if (a.getHistoricoId().equals(historicoId)) {
                resultado.add(a);
            }
        }
        return resultado;
    }

    public Optional<Avaliacao> buscarPorId(String id) {
        return avaliacoes.stream().filter(a -> a.getId().equals(id)).findFirst();
    }
}