package com.marketplace.repository;

import com.google.gson.*;
import org.apache.commons.io.IOUtils;
import java.io.*;
import java.util.ArrayList;

public class HistoricoRepository {
    Class c;
    Gson gson;

    public HistoricoRepository(Class c) {
        // dataBase = nome do arquivo que guarda os dados | c = classe que representa os
        // objetos daquele arquivo (usar .class para o parametro)
        this.c = c;
        gson = new Gson();
    }

    public Object[] parseData() {
        String content;
        JsonArray jsonArray;

        String path = "historico-v1.json";
        File file = new File(path);
        // Se o arquivo não existe ou está vazio
        if (!file.exists() || file.length() == 0) {
            // Cria estrutura inicial se não existir
            if (!file.exists()) {
                try (Writer writer = new FileWriter(path)) {
                    JsonObject initialStructure = new JsonObject();
                    initialStructure.add("historico", new JsonArray());
                    gson.toJson(initialStructure, writer);
                } catch (IOException e) {
                    throw new RuntimeException("Erro ao criar arquivo de histórico", e);
                }
            }
            return new Object[0];
        }
        try (FileReader fileReader = new FileReader(path)) {
            content = IOUtils.toString(fileReader);
            if (content.isBlank()) {
                return new Object[0];
            }
        } catch (FileNotFoundException e) {
            System.out.println("Arquivo não encontrado");
            return new Object[0];
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        JsonObject jsonObject = gson.fromJson(content, JsonObject.class);
        jsonArray = jsonObject.getAsJsonArray("historico");

        Object[] objects = new Object[jsonArray.size()];

        for (int i = 0; i < jsonArray.size(); i++) {
            objects[i] = gson.fromJson(jsonArray.get(i), c);
        }

        return objects;
    }

    public void writeData(Object[] arr) {
        JsonArray jsonArray = new JsonArray();
        for (Object o : arr) {
            jsonArray.add(gson.toJsonTree(o, c));
        }

        JsonObject jsonObject = new JsonObject();
        jsonObject.add("historico", jsonArray);

        String path = "historico-v1.json";

        try (Writer writer = new FileWriter(path)) {
            gson.toJson(jsonObject, writer);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}