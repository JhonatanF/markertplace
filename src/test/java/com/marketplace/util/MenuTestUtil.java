package com.marketplace.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.JsonNode;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class MenuTestUtil {
    private static final ObjectMapper mapper = new ObjectMapper();
    private static JsonNode testCases;

    static {
        try {
            InputStream is = MenuTestUtil.class.getResourceAsStream("/menu-test-cases.json");
            testCases = mapper.readTree(is);
        } catch (Exception e) {
            throw new RuntimeException("Failed to load menu test cases", e);
        }
    }

    public static List<String> getInputsForAction(String menu, String action) {
        List<String> inputs = new ArrayList<>();
        JsonNode menuNode = testCases.get(menu);
        JsonNode options = menuNode.get("options");
        
        for (JsonNode option : options) {
            if (option.get("action").asText().equals(action)) {
                inputs.add(option.get("option").asText());
                if (option.has("inputs")) {
                    option.get("inputs").forEach(input -> 
                        inputs.add(input.get("value").asText())
                    );
                }
                break;
            }
        }
        
        return inputs;
    }

    public static String getMenuTitle(String menu) {
        return testCases.get(menu).get("title").asText();
    }

    public static List<String> getInvalidInput(String menu, String testCase) {
        List<String> inputs = new ArrayList<>();
        JsonNode invalidCases = testCases.get(menu).get("invalidCases");
        
        for (JsonNode invalidCase : invalidCases) {
            if (invalidCase.get("action").asText().equals(testCase)) {
                inputs.add(invalidCase.get("input").asText());
                break;
            }
        }
        
        return inputs;
    }
}