package com.pizzasystem.services;

import com.pizzasystem.models.Order;
import com.pizzasystem.models.Pizza;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

import static org.junit.jupiter.api.Assertions.*;

public class PaymentProcessorTest {

    private DatabaseManager db;
    private PaymentProcessor paymentProcessor;

    @BeforeEach
    public void setup() {
        db = new DatabaseManager("jdbc:h2:mem:testdb", "sa", "");
        db.connect();
        paymentProcessor = new PaymentProcessor(db);
    }

    @AfterEach
    public void tearDown() {
        db.disconnect();
    }

    @Test
    public void testCalculateTotal() {
        Order order = new Order(1L, 1L);
        Pizza pizza1 = new Pizza(1L, "Margherita", "Small", null, 8.0);
        Pizza pizza2 = new Pizza(2L, "Pepperoni", "Medium", null, 10.0);
        order.addPizza(pizza1);
        order.addPizza(pizza2);

        double total = paymentProcessor.calculateTotal(order);
        assertEquals(18.0, total, 0.001, "El total debe ser la suma de los precios de las pizzas");
    }

    @Test
    public void testProcessPaymentAndRefund() {
        Order order = new Order(2L, 1L);
        Pizza pizza = new Pizza(3L, "Veggie", "Large", null, 9.5);
        order.addPizza(pizza);

        // Guardar la orden para que la operación de update funcione
        db.save(order);

        boolean processed = paymentProcessor.processPayment(order, "Credit Card", "cardDetails");
        assertTrue(processed, "El pago debe procesarse correctamente");
        assertTrue(order.isPaid(), "La orden debe marcarse como pagada");
        assertEquals("PAID", order.getStatus(), "El estado debe ser PAID tras el pago");

        boolean refunded = paymentProcessor.refundPayment(order);
        assertTrue(refunded, "El reembolso debe procesarse correctamente");
        assertFalse(order.isPaid(), "La orden debe marcarse como no pagada tras el reembolso");
        assertEquals("REFUNDED", order.getStatus(), "El estado debe ser REFUNDED tras el reembolso");
    }
}
