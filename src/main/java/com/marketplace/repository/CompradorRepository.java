package com.marketplace.repository;

import com.marketplace.model.Comprador;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CompradorRepository extends AbstractFileRepository<Comprador, String> {
    private static final Pattern JSON_OBJECT_PATTERN = Pattern.compile("\\{([^{}]|\\{[^{}]*\\})*\\}");
    private static final Pattern STRING_FIELD_PATTERN = Pattern.compile("\"([^\"]+)\"\\s*:\\s*\"([^\"]+)\"");
    private static final Pattern NUMBER_FIELD_PATTERN = Pattern.compile("\"([^\"]+)\"\\s*:\\s*(\\d+)");

    public CompradorRepository() {
        super("compradores-v1.json");
    }

    @Override
    protected String getId(Comprador entity) {
        return entity.getCpf();
    }

    @Override
    protected Map<String, Comprador> parseJsonToMap(String json) {
        Map<String, Comprador> result = new HashMap<>();
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
            Comprador comprador = parseJsonToComprador(objectJson);
            if (comprador != null) {
                result.put(comprador.getCpf(), comprador);
            }
        }

        return result;
    }

    private Comprador parseJsonToComprador(String json) {
        Map<String, String> fields = new HashMap<>();
        
        // Parse string fields
        Matcher stringFieldMatcher = STRING_FIELD_PATTERN.matcher(json);
        while (stringFieldMatcher.find()) {
            String key = stringFieldMatcher.group(1);
            String value = stringFieldMatcher.group(2);
            fields.put(key, value);
        }

        // Parse numeric fields
        Matcher numberFieldMatcher = NUMBER_FIELD_PATTERN.matcher(json);
        while (numberFieldMatcher.find()) {
            String key = numberFieldMatcher.group(1);
            String value = numberFieldMatcher.group(2);
            fields.put(key, value);
        }

        if (fields.containsKey("nome") && fields.containsKey("email") && 
            fields.containsKey("senha") && fields.containsKey("cpf") && 
            fields.containsKey("endereco")) {
            Comprador comprador = new Comprador(
                fields.get("nome"),
                fields.get("email"),
                fields.get("senha"),
                fields.get("cpf"),
                fields.get("endereco")
            );
            if (fields.containsKey("pontuacao")) {
                comprador.setPontuacao(Integer.parseInt(fields.get("pontuacao").trim()));
            }
            return comprador;
        }
        return null;
    }

    @Override
    protected String convertMapToJson() {
        StringBuilder json = new StringBuilder();
        json.append("{\n");
        
        int count = 0;
        for (Map.Entry<String, Comprador> entry : entities.entrySet()) {
            if (count > 0) {
                json.append(",\n");
            }
            
            Comprador comprador = entry.getValue();
            json.append("  \"").append(entry.getKey()).append("\": {\n");
            json.append("    \"nome\": \"").append(escape(comprador.getNome())).append("\",\n");
            json.append("    \"email\": \"").append(escape(comprador.getEmail())).append("\",\n");
            json.append("    \"senha\": \"").append(escape(comprador.getSenha())).append("\",\n");
            json.append("    \"cpf\": \"").append(escape(comprador.getCpf())).append("\",\n");
            json.append("    \"endereco\": \"").append(escape(comprador.getEndereco())).append("\",\n");
            json.append("    \"pontuacao\": ").append(comprador.getPontuacao()).append("\n");
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

    @Override
    protected Class<String> getIdClass() {
        return String.class;
    }

    @Override
    protected Class<Comprador> getEntityClass() {
        return Comprador.class;
    }
}
