package com.pizzasystem.services;

import com.pizzasystem.interfaces.IDatabaseManager;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.HashMap;
import java.util.Map;

public class DatabaseManager implements IDatabaseManager {
    private String url;
    private String username;
    private String password;
    private Connection connection;

    // Simulación de base de datos en memoria para demostración
    private Map<Class<?>, Map<Long, Object>> inMemoryDb = new HashMap<>();

    public DatabaseManager(String url, String username, String password) {
        this.url = url;
        this.username = username;
        this.password = password;
    }

    @Override
    public boolean connect() {
        try {
            // En una aplicación real, establecería una conexión real a la base de datos
            this.connection = DriverManager.getConnection(url, username, password);
            return true;
        } catch (SQLException e) {
            System.err.println("Error al conectar a la base de datos: " + e.getMessage());
            return false;
        }
    }

    @Override
    public void disconnect() {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                System.err.println("Error al cerrar la conexión: " + e.getMessage());
            }
        }
    }

    @Override
    public <T> boolean save(T entity) {
        try {
            Class<?> entityClass = entity.getClass();
            Long id = (Long) entityClass.getMethod("getId").invoke(entity);

            if (!inMemoryDb.containsKey(entityClass)) {
                inMemoryDb.put(entityClass, new HashMap<>());
            }

            inMemoryDb.get(entityClass).put(id, entity);
            return true;
        } catch (Exception e) {
            System.err.println("Error al guardar entidad: " + e.getMessage());
            return false;
        }
    }

    @Override
    public <T> boolean update(T entity) {
        return save(entity); // Simplificado para este ejemplo
    }

    @Override
    public <T> boolean delete(T entity) {
        try {
            Class<?> entityClass = entity.getClass();
            Long id = (Long) entityClass.getMethod("getId").invoke(entity);

            if (inMemoryDb.containsKey(entityClass) && inMemoryDb.get(entityClass).containsKey(id)) {
                inMemoryDb.get(entityClass).remove(id);
                return true;
            }
            return false;
        } catch (Exception e) {
            System.err.println("Error al eliminar entidad: " + e.getMessage());
            return false;
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> Optional<T> findById(Class<T> type, Long id) {
        if (inMemoryDb.containsKey(type) && inMemoryDb.get(type).containsKey(id)) {
            return Optional.of((T) inMemoryDb.get(type).get(id));
        }
        return Optional.empty();
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> List<T> findAll(Class<T> type) {
        List<T> results = new ArrayList<>();
        if (inMemoryDb.containsKey(type)) {
            inMemoryDb.get(type).values().forEach(entity -> results.add((T) entity));
        }
        return results;
    }
}
