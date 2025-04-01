package com.pizzasystem.models;

import java.time.LocalDateTime;
import java.util.List;
import java.util.ArrayList;

public class Order {
    private Long id;
    private Long userId;
    private List<Pizza> pizzas;
    private String status;
    private LocalDateTime orderTime;
    private LocalDateTime deliveryTime;
    private double totalAmount;
    private boolean isPaid;
    private String paymentMethod;

    // Constructor
    public Order(Long id, Long userId) {
        this.id = id;
        this.userId = userId;
        this.pizzas = new ArrayList<>();
        this.status = "PENDING";
        this.orderTime = LocalDateTime.now();
        this.isPaid = false;
    }

    // Getters y setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    public List<Pizza> getPizzas() { return pizzas; }
    public void setPizzas(List<Pizza> pizzas) { this.pizzas = pizzas; }

    public void addPizza(Pizza pizza) {
        if (pizzas == null) {
            pizzas = new ArrayList<>();
        }
        pizzas.add(pizza);
    }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public LocalDateTime getOrderTime() { return orderTime; }
    public void setOrderTime(LocalDateTime orderTime) { this.orderTime = orderTime; }

    public LocalDateTime getDeliveryTime() { return deliveryTime; }
    public void setDeliveryTime(LocalDateTime deliveryTime) { this.deliveryTime = deliveryTime; }

    public double getTotalAmount() { return totalAmount; }
    public void setTotalAmount(double totalAmount) { this.totalAmount = totalAmount; }

    public boolean isPaid() { return isPaid; }
    public void setPaid(boolean isPaid) { this.isPaid = isPaid; }

    public String getPaymentMethod() { return paymentMethod; }
    public void setPaymentMethod(String paymentMethod) { this.paymentMethod = paymentMethod; }
}