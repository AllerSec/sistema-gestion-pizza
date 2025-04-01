package com.pizzasystem.services;

import com.pizzasystem.interfaces.IPaymentProcessor;
import com.pizzasystem.interfaces.IDatabaseManager;
import com.pizzasystem.models.Order;
import com.pizzasystem.models.Pizza;

public class PaymentProcessor implements IPaymentProcessor {
    private final IDatabaseManager databaseManager;

    public PaymentProcessor(IDatabaseManager databaseManager) {
        this.databaseManager = databaseManager;
    }

    @Override
    public boolean processPayment(Order order, String paymentMethod, String paymentDetails) {
        // Simulación de procesamiento de pago
        if (order != null && paymentMethod != null && !paymentMethod.isEmpty()) {
            order.setTotalAmount(calculateTotal(order));
            order.setPaymentMethod(paymentMethod);
            order.setPaid(true);
            order.setStatus("PAID");

            return databaseManager.update(order);
        }
        return false;
    }

    @Override
    public boolean refundPayment(Order order) {
        if (order != null && order.isPaid()) {
            order.setPaid(false);
            order.setStatus("REFUNDED");

            return databaseManager.update(order);
        }
        return false;
    }

    @Override
    public double calculateTotal(Order order) {
        if (order == null || order.getPizzas() == null) {
            return 0.0;
        }

        return order.getPizzas().stream()
                .mapToDouble(Pizza::getPrice)
                .sum();
    }
}