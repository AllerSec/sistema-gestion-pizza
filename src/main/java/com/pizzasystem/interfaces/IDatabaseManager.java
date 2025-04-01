
package com.pizzasystem.interfaces;

import java.util.List;
import java.util.Optional;

public interface IDatabaseManager {
    <T> boolean save(T entity);
    <T> boolean update(T entity);
    <T> boolean delete(T entity);
    <T> Optional<T> findById(Class<T> type, Long id);
    <T> List<T> findAll(Class<T> type);
    boolean connect();
    void disconnect();
}