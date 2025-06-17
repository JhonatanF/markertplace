package com.marketplace.repository;

import com.google.gson.*;
import org.apache.commons.io.IOUtils;
import com.marketplace.model.Historico;

import java.io.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

public class HistoricoRepository {
    private final String path = "historicos-v1.json";
    Class c;
    private final Gson gson = new GsonBuilder()
            .registerTypeAdapter(Date.class,
                    (JsonSerializer<Date>) (date, type, context) -> new JsonPrimitive(date.getTime()))
            .registerTypeAdapter(Date.class,
                    (JsonDeserializer<Date>) (json, type, context) -> new Date(json.getAsLong()))
            .setPrettyPrinting()
            .create();

    public HistoricoRepository(Class c) {
        // dataBase = nome do arquivo que guarda os dados | c = classe que representa os
        // objetos daquele arquivo (usar .class para o parametro)
        this.c = c;
    }

    public Historico salvar(Historico historico) {
        List<Historico> historicos = listarTodos();

        historicos.removeIf(h -> h.getCompradorCpf().equals(historico.getCompradorCpf()));
        historicos.add(historico);

        salvarTodos(historicos);
        return historico;
    }

    public Optional<Historico> buscarPorComprador(String compradorCpf) {
        return listarTodos().stream()
                .filter(h -> h.getCompradorCpf().equals(compradorCpf))
                .findFirst();
    }

    public List<Historico> listarTodos() {
        try (FileReader reader = new FileReader(path)) {
            String content = IOUtils.toString(reader);
            if (content.isBlank())
                return new ArrayList<>();

            JsonObject jsonObject = gson.fromJson(content, JsonObject.class);
            JsonArray jsonArray = jsonObject.getAsJsonArray("historicos");

            List<Historico> result = new ArrayList<>();
            for (JsonElement elem : jsonArray) {
                result.add(gson.fromJson(elem, Historico.class));
            }
            return result;
        } catch (FileNotFoundException e) {
            return new ArrayList<>();
        } catch (IOException e) {
            throw new RuntimeException("Erro ao ler arquivo de históricos", e);
        }
    }

    private void salvarTodos(List<Historico> historicos) {
        JsonArray jsonArray = new JsonArray();
        for (Historico h : historicos) {
            jsonArray.add(gson.toJsonTree(h));
        }

        JsonObject jsonObject = new JsonObject();
        jsonObject.add("historicos", jsonArray);

        try (Writer writer = new FileWriter(path)) {
            gson.toJson(jsonObject, writer);
        } catch (IOException e) {
            throw new RuntimeException("Erro ao salvar arquivo de históricos", e);
        }
    }

    public void removerHistoricos() {
        salvarTodos(new ArrayList<>());
    }
}
