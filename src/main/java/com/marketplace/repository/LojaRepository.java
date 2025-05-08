package com.marketplace.repository;

import com.marketplace.model.Loja;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LojaRepository extends AbstractFileRepository<Loja, String> {
    private static final Pattern JSON_OBJECT_PATTERN = Pattern.compile("\\{([^{}]|\\{[^{}]*\\})*\\}");
    private static final Pattern FIELD_PATTERN = Pattern.compile("\"([^\"]+)\"\\s*:\\s*\"([^\"]+)\"");

    public LojaRepository() {
        super("lojas-v1.json");
    }

    @Override
    protected String getId(Loja entity) {
        return entity.getCpfCnpj();
    }

    @Override
    protected Map<String, Loja> parseJsonToMap(String json) {
        Map<String, Loja> result = new HashMap<>();
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
            Loja loja = parseJsonToLoja(objectJson);
            if (loja != null) {
                result.put(loja.getCpfCnpj(), loja);
            }
        }

        return result;
    }

    private Loja parseJsonToLoja(String json) {
        Map<String, String> fields = new HashMap<>();
        Matcher fieldMatcher = FIELD_PATTERN.matcher(json);
        
        while (fieldMatcher.find()) {
            String key = fieldMatcher.group(1);
            String value = fieldMatcher.group(2);
            fields.put(key, value);
        }

        if (fields.containsKey("nome") && fields.containsKey("email") && 
            fields.containsKey("senha") && fields.containsKey("cpfCnpj") && 
            fields.containsKey("endereco")) {
            return new Loja(
                fields.get("nome"),
                fields.get("email"),
                fields.get("senha"),
                fields.get("cpfCnpj"),
                fields.get("endereco")
            );
        }
        return null;
    }

    @Override
    protected String convertMapToJson() {
        StringBuilder json = new StringBuilder();
        json.append("{\n");
        
        int count = 0;
        for (Map.Entry<String, Loja> entry : entities.entrySet()) {
            if (count > 0) {
                json.append(",\n");
            }
            
            Loja loja = entry.getValue();
            json.append("  \"").append(entry.getKey()).append("\": {\n");
            json.append("    \"nome\": \"").append(escape(loja.getNome())).append("\",\n");
            json.append("    \"email\": \"").append(escape(loja.getEmail())).append("\",\n");
            json.append("    \"senha\": \"").append(escape(loja.getSenha())).append("\",\n");
            json.append("    \"cpfCnpj\": \"").append(escape(loja.getCpfCnpj())).append("\",\n");
            json.append("    \"endereco\": \"").append(escape(loja.getEndereco())).append("\"\n");
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
    protected Class<Loja> getEntityClass() {
        return Loja.class;
    }
}
