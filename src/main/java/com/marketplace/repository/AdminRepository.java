package com.marketplace.repository;

import com.marketplace.model.Admin;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AdminRepository extends AbstractFileRepository<Admin, String> {
    private static final Pattern JSON_OBJECT_PATTERN = Pattern.compile("\\{([^{}]|\\{[^{}]*\\})*\\}");
    private static final Pattern FIELD_PATTERN = Pattern.compile("\"([^\"]+)\"\\s*:\\s*\"([^\"]+)\"");

    public AdminRepository() {
        super("admins-v1.json");
    }

    @Override
    protected String getId(Admin entity) {
        return entity.getCpf();
    }

    @Override
    protected Map<String, Admin> parseJsonToMap(String json) {
        Map<String, Admin> result = new HashMap<>();
        if (json == null || json.trim().isEmpty()) {
            return result;
        }

        // Remove leading/trailing spaces and brackets
        json = json.trim();
        if (json.startsWith("{")) {
            json = json.substring(1);
        }
        if (json.endsWith("}")) {
            json = json.substring(0, json.length() - 1);
        }

        // Find all JSON objects
        Matcher objectMatcher = JSON_OBJECT_PATTERN.matcher(json);
        while (objectMatcher.find()) {
            String objectJson = objectMatcher.group();
            Admin admin = parseJsonToAdmin(objectJson);
            if (admin != null) {
                result.put(admin.getCpf(), admin);
            }
        }

        return result;
    }

    private Admin parseJsonToAdmin(String json) {
        Map<String, String> fields = new HashMap<>();
        Matcher fieldMatcher = FIELD_PATTERN.matcher(json);
        
        while (fieldMatcher.find()) {
            String key = fieldMatcher.group(1);
            String value = fieldMatcher.group(2);
            fields.put(key, value);
        }

        if (fields.containsKey("nome") && fields.containsKey("email") && 
            fields.containsKey("senha") && fields.containsKey("cpf") && 
            fields.containsKey("endereco")) {
            return new Admin(
                fields.get("nome"),
                fields.get("email"),
                fields.get("senha"),
                fields.get("cpf"),
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
        for (Map.Entry<String, Admin> entry : entities.entrySet()) {
            if (count > 0) {
                json.append(",\n");
            }
            
            Admin admin = entry.getValue();
            json.append("  \"").append(entry.getKey()).append("\": {\n");
            json.append("    \"nome\": \"").append(escape(admin.getNome())).append("\",\n");
            json.append("    \"email\": \"").append(escape(admin.getEmail())).append("\",\n");
            json.append("    \"senha\": \"").append(escape(admin.getSenha())).append("\",\n");
            json.append("    \"cpf\": \"").append(escape(admin.getCpf())).append("\",\n");
            json.append("    \"endereco\": \"").append(escape(admin.getEndereco())).append("\"\n");
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
    protected Class<Admin> getEntityClass() {
        return Admin.class;
    }
}