package com.pizzasystem.interfaces;

import com.pizzasystem.models.Order;

public interface IPaymentProcessor {
    boolean processPayment(Order order, String paymentMethod, String paymentDetails);
    boolean refundPayment(Order order);
    double calculateTotal(Order order);
}