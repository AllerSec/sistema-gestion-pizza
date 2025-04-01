package com.pizzasystem.services;

import com.pizzasystem.models.Order;
import com.pizzasystem.models.Pizza;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

import java.util.Optional;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class OrderManagerTest {

    private DatabaseManager db;
    private OrderManager orderManager;

    @BeforeEach
    public void setup() {
        db = new DatabaseManager("jdbc:h2:mem:testdb", "sa", "");
        db.connect();
        orderManager = new OrderManager(db);
    }

    @AfterEach
    public void tearDown() {
        db.disconnect();
    }

    @Test
    public void testCreateAndRetrieveOrder() {
        Order order = new Order(1L, 1L);
        Pizza pizza = new Pizza(1L, "Pepperoni", "Large", null, 12.0);
        order.addPizza(pizza);

        boolean created = orderManager.createOrder(order);
        assertTrue(created, "La orden debe crearse correctamente");

        Optional<Order> retrieved = orderManager.getOrder(1L);
        assertTrue(retrieved.isPresent(), "La orden debe recuperarse por su ID");
        assertEquals("PENDING", retrieved.get().getStatus(), "El estado inicial debe ser PENDING");
    }

    @Test
    public void testUpdateAndCancelOrder() {
        Order order = new Order(2L, 1L);
        orderManager.createOrder(order);

        // Actualizar el estado de la orden a IN_PROGRESS
        order.setStatus("IN_PROGRESS");
        boolean updated = orderManager.updateOrder(order);
        assertTrue(updated, "La orden debe actualizarse correctamente");

        Optional<Order> updatedOrder = orderManager.getOrder(2L);
        assertTrue(updatedOrder.isPresent(), "La orden actualizada debe recuperarse");
        assertEquals("IN_PROGRESS", updatedOrder.get().getStatus(), "El estado debe ser IN_PROGRESS");

        // Cancelar la orden
        boolean cancelled = orderManager.cancelOrder(2L);
        assertTrue(cancelled, "La orden debe cancelarse correctamente");

        Optional<Order> cancelledOrder = orderManager.getOrder(2L);
        assertTrue(cancelledOrder.isPresent(), "La orden cancelada debe seguir siendo recuperable");
        assertEquals("CANCELLED", cancelledOrder.get().getStatus(), "El estado debe ser CANCELLED tras la cancelación");
    }

    @Test
    public void testGetUserOrders() {
        // Crear dos órdenes para el usuario 1 y una para el usuario 2
        Order order1 = new Order(3L, 1L);
        Order order2 = new Order(4L, 1L);
        Order order3 = new Order(5L, 2L);

        orderManager.createOrder(order1);
        orderManager.createOrder(order2);
        orderManager.createOrder(order3);

        List<Order> user1Orders = orderManager.getUserOrders(1L);
        assertEquals(2, user1Orders.size(), "El usuario 1 debe tener 2 órdenes");

        List<Order> user2Orders = orderManager.getUserOrders(2L);
        assertEquals(1, user2Orders.size(), "El usuario 2 debe tener 1 orden");
    }
}
