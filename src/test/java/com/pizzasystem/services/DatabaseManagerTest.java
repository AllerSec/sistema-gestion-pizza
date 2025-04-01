package com.pizzasystem.services;

import com.pizzasystem.models.Pizza;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

import java.util.Optional;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class DatabaseManagerTest {

    private DatabaseManager db;

    @BeforeEach
    public void setup() {
        db = new DatabaseManager("jdbc:h2:mem:testdb", "sa", "");
        db.connect();
    }

    @AfterEach
    public void tearDown() {
        db.disconnect();
    }

    @Test
    public void testSaveAndFindById() {
        Pizza pizza = new Pizza(1L, "Margarita", "Medium", null, 8.5);
        boolean saved = db.save(pizza);
        assertTrue(saved, "La pizza debe guardarse correctamente");

        Optional<Pizza> retrieved = db.findById(Pizza.class, 1L);
        assertTrue(retrieved.isPresent(), "La pizza debe encontrarse por su ID");
        assertEquals("Margarita", retrieved.get().getName(), "El nombre de la pizza debe coincidir");
    }

    @Test
    public void testUpdate() {
        Pizza pizza = new Pizza(2L, "Pepperoni", "Large", null, 10.0);
        db.save(pizza);

        // Actualizar el nombre de la pizza
        pizza.setName("Spicy Pepperoni");
        boolean updated = db.update(pizza);
        assertTrue(updated, "La pizza debe actualizarse correctamente");

        Optional<Pizza> retrieved = db.findById(Pizza.class, 2L);
        assertTrue(retrieved.isPresent(), "La pizza actualizada debe encontrarse");
        assertEquals("Spicy Pepperoni", retrieved.get().getName(), "El nombre debe haber sido actualizado");
    }

    @Test
    public void testDelete() {
        Pizza pizza = new Pizza(3L, "Four Cheese", "Small", null, 9.0);
        db.save(pizza);
        boolean deleted = db.delete(pizza);
        assertTrue(deleted, "La pizza debe eliminarse correctamente");

        Optional<Pizza> retrieved = db.findById(Pizza.class, 3L);
        assertFalse(retrieved.isPresent(), "La pizza no debe encontrarse tras eliminarla");
    }

    @Test
    public void testFindAll() {
        Pizza pizza1 = new Pizza(4L, "Veggie", "Medium", null, 8.0);
        Pizza pizza2 = new Pizza(5L, "Hawaiian", "Large", null, 11.0);
        db.save(pizza1);
        db.save(pizza2);

        List<Pizza> pizzas = db.findAll(Pizza.class);
        assertEquals(2, pizzas.size(), "Debe recuperar todas las pizzas guardadas");
    }
}
