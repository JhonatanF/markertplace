package com.marketplace.repository;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public abstract class AbstractFileRepository<T, ID> implements Repository<T, ID> {
    protected final String filename;
    protected Map<ID, T> entities;

    protected AbstractFileRepository(String filename) {
        this.filename = filename;
        this.entities = new HashMap<>();
        loadFromFile();
    }

    @SuppressWarnings("unchecked")
    private void loadFromFile() {
        File file = new File(filename);
        if (file.exists()) {
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
                entities = (Map<ID, T>) ois.readObject();
            } catch (IOException | ClassNotFoundException e) {
                System.err.println("Erro ao carregar dados do arquivo: " + e.getMessage());
                entities = new HashMap<>();
            }
        }
    }

    private void saveToFile() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filename))) {
            oos.writeObject(entities);
        } catch (IOException e) {
            System.err.println("Erro ao salvar dados no arquivo: " + e.getMessage());
        }
    }

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

    protected abstract ID getId(T entity);
}
