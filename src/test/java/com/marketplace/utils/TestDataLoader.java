package com.marketplace.utils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.marketplace.model.Comprador;
import com.marketplace.model.Loja;
import com.marketplace.model.Produto;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class TestDataLoader {
    private static final ObjectMapper mapper = new ObjectMapper();
    private static JsonNode testData;

    static {
        try (InputStream is = TestDataLoader.class.getResourceAsStream("/test-data.json")) {
            testData = mapper.readTree(is);
        } catch (IOException e) {
            throw new RuntimeException("Failed to load test data", e);
        }
    }

    public static List<Loja> loadLojas() {
        List<Loja> lojas = new ArrayList<>();
        testData.get("lojas").forEach(node -> {
            Loja loja = new Loja(
                node.get("nome").asText(),
                node.get("email").asText(),
                node.get("senha").asText(),
                node.get("cpfCnpj").asText(),
                node.get("endereco").asText()
            );
            lojas.add(loja);
        });
        return lojas;
    }

    public static List<Comprador> loadCompradores() {
        List<Comprador> compradores = new ArrayList<>();
        testData.get("compradores").forEach(node -> {
            Comprador comprador = new Comprador(
                node.get("nome").asText(),
                node.get("email").asText(),
                node.get("senha").asText(),
                node.get("cpf").asText(),
                node.get("endereco").asText()
            );
            compradores.add(comprador);
        });
        return compradores;
    }

    public static List<Produto> loadProdutos() {
        List<Produto> produtos = new ArrayList<>();
        testData.get("produtos").forEach(node -> {
            Produto produto = new Produto(
                node.get("nome").asText(),
                node.get("valor").asDouble(),
                node.get("tipo").asText(),
                node.get("quantidade").asInt(),
                node.get("marca").asText(),
                node.get("descricao").asText(),
                node.get("lojaCpfCnpj").asText()
            );
            produtos.add(produto);
        });
        return produtos;
    }
}
