package com.marketplace.repository;

import com.google.gson.*;
import org.apache.commons.io.IOUtils;
import java.io.*;
import java.util.ArrayList;

public class HistoricoRepository {
    Class c;
    Gson gson;

    public HistoricoRepository(Class c) {
        // dataBase = nome do arquivo que guarda os dados | c = classe que representa os objetos daquele arquivo (usar .class para o parametro)
        this.c = c;
        gson = new Gson();
    }

    public Object[] parseData(){
        String content = new String();
        JsonArray jsonArray;

        String path = "historico-v1.json";

        try {
            FileReader fileReader = new FileReader(path);
            content = IOUtils.toString(fileReader);
        } catch (FileNotFoundException e) {
            System.out.println(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }finally {
            jsonArray = this.gson.fromJson(content, JsonArray.class);
        }

        Object[] objects = new Object[jsonArray.size()];

        for (int i = 0; i < jsonArray.size(); i++) {
            objects[i] = gson.fromJson(jsonArray.get(i), c);
        }

        return objects;
    }

    public void writeData(Object[] arr) {
        JsonArray jsonArray = new JsonArray();
        for (int i = 0; i < arr.length; i++) {
            jsonArray.add(gson.toJson(arr[i],c));
        }

        String path = "historico-v1.json";

        try (Writer writer = new FileWriter(path)) {
            Gson gson = new GsonBuilder().create();
            gson.toJson(arr, writer);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}