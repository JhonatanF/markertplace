package com.marketplace.repository;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public abstract class AbstractFileRepository<T, ID> implements Repository<T, ID> {
    protected final String filename;
    protected Map<ID, T> entities;

    protected AbstractFileRepository(String filename) {
        this.filename = filename;
        this.entities = new HashMap<>();
        loadFromFile();
    }

    private void loadFromFile() {
        File file = new File(filename);
        if (file.exists()) {
            try (BufferedReader reader = new BufferedReader(new FileReader(file, StandardCharsets.UTF_8))) {
                String jsonStr = reader.lines().collect(Collectors.joining("\n"));
                entities = parseJsonToMap(jsonStr);
            } catch (IOException e) {
                System.err.println("Error loading data from file: " + e.getMessage());
                entities = new HashMap<>();
            }
        }
    }

    private void saveToFile() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename, StandardCharsets.UTF_8))) {
            String jsonStr = convertMapToJson();
            writer.write(jsonStr);
        } catch (IOException e) {
            System.err.println("Error saving data to file: " + e.getMessage());
        }
    }

    protected abstract Map<ID, T> parseJsonToMap(String json);
    protected abstract String convertMapToJson();
    protected abstract ID getId(T entity);
    protected abstract Class<ID> getIdClass();
    protected abstract Class<T> getEntityClass();

    @Override
    public T save(T entity) {
        ID id = getId(entity);
        entities.put(id, entity);
        saveToFile();
        return entity;
    }

    @Override
    public Optional<T> findById(ID id) {
        return Optional.ofNullable(entities.get(id));
    }

    @Override
    public List<T> findAll() {
        return new ArrayList<>(entities.values());
    }

    @Override
    public void delete(ID id) {
        entities.remove(id);
        saveToFile();
    }

    @Override
    public boolean exists(ID id) {
        return entities.containsKey(id);
    }
}
