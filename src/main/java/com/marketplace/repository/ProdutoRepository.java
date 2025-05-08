package com.marketplace.repository;

import com.marketplace.model.Produto;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class ProdutoRepository extends AbstractFileRepository<Produto, String> {
    private static final Pattern JSON_OBJECT_PATTERN = Pattern.compile("\\{([^{}]|\\{[^{}]*\\})*\\}");
    private static final Pattern FIELD_PATTERN = Pattern.compile("\"([^\"]+)\"\\s*:\\s*\"?([^\"\\},]+)\"?");

    public ProdutoRepository() {
        super("produtos-v1.json");
    }

    @Override
    protected String getId(Produto entity) {
        return entity.getId();
    }

    @Override
    protected Map<String, Produto> parseJsonToMap(String json) {
        Map<String, Produto> result = new HashMap<>();
        if (json == null || json.trim().isEmpty()) {
            return result;
        }

        json = json.trim();
        if (json.startsWith("{")) {
            json = json.substring(1);
        }
        if (json.endsWith("}")) {
            json = json.substring(0, json.length() - 1);
        }

        Matcher objectMatcher = JSON_OBJECT_PATTERN.matcher(json);
        while (objectMatcher.find()) {
            String objectJson = objectMatcher.group();
            Produto produto = parseJsonToProduto(objectJson);
            if (produto != null) {
                result.put(produto.getId(), produto);
            }
        }

        return result;
    }

    private Produto parseJsonToProduto(String json) {
        Map<String, String> fields = new HashMap<>();
        Matcher fieldMatcher = FIELD_PATTERN.matcher(json);
        
        while (fieldMatcher.find()) {
            String key = fieldMatcher.group(1);
            String value = fieldMatcher.group(2);
            fields.put(key, value);
        }

        if (fields.containsKey("id") && fields.containsKey("nome") && 
            fields.containsKey("valor") && fields.containsKey("tipo") && 
            fields.containsKey("quantidade") && fields.containsKey("marca") && 
            fields.containsKey("descricao") && fields.containsKey("lojaCpfCnpj")) {
            return new Produto(
                fields.get("id"),
                fields.get("nome"),
                Double.parseDouble(fields.get("valor")),
                fields.get("tipo"),
                Integer.parseInt(fields.get("quantidade")),
                fields.get("marca"),
                fields.get("descricao"),
                fields.get("lojaCpfCnpj")
            );
        }
        return null;
    }

    @Override
    protected String convertMapToJson() {
        StringBuilder json = new StringBuilder();
        json.append("{\n");
        
        int count = 0;
        for (Map.Entry<String, Produto> entry : entities.entrySet()) {
            if (count > 0) {
                json.append(",\n");
            }
            
            Produto produto = entry.getValue();
            json.append("  \"").append(entry.getKey()).append("\": {\n");
            json.append("    \"id\": \"").append(escape(produto.getId())).append("\",\n");
            json.append("    \"nome\": \"").append(escape(produto.getNome())).append("\",\n");
            json.append("    \"valor\": ").append(produto.getValor()).append(",\n");
            json.append("    \"tipo\": \"").append(escape(produto.getTipo())).append("\",\n");
            json.append("    \"quantidade\": ").append(produto.getQuantidade()).append(",\n");
            json.append("    \"marca\": \"").append(escape(produto.getMarca())).append("\",\n");
            json.append("    \"descricao\": \"").append(escape(produto.getDescricao())).append("\",\n");
            json.append("    \"lojaCpfCnpj\": \"").append(escape(produto.getLojaCpfCnpj())).append("\"\n");
            json.append("  }");
            
            count++;
        }
        
        json.append("\n}");
        return json.toString();
    }

    private String escape(String str) {
        return str.replace("\"", "\\\"")
                 .replace("\n", "\\n")
                 .replace("\r", "\\r")
                 .replace("\t", "\\t");
    }

    public List<Produto> findByLoja(String lojaCpfCnpj) {
        return findAll().stream()
                .filter(produto -> produto.getLojaCpfCnpj().equals(lojaCpfCnpj))
                .collect(Collectors.toList());
    }

    @Override
    protected Class<String> getIdClass() {
        return String.class;
    }

    @Override
    protected Class<Produto> getEntityClass() {
        return Produto.class;
    }
}
