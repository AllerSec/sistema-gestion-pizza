package com.pizzasystem.services;

import com.pizzasystem.interfaces.IDatabaseManager;
import com.pizzasystem.interfaces.IOrderManager;
import com.pizzasystem.models.Order;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class OrderManager implements IOrderManager {
    private final IDatabaseManager databaseManager;

    public OrderManager(IDatabaseManager databaseManager) {
        this.databaseManager = databaseManager;
    }

    @Override
    public boolean createOrder(Order order) {
        return databaseManager.save(order);
    }

    @Override
    public boolean updateOrder(Order order) {
        return databaseManager.update(order);
    }

    @Override
    public boolean cancelOrder(Long orderId) {
        Optional<Order> orderOpt = databaseManager.findById(Order.class, orderId);
        if (orderOpt.isPresent()) {
            Order order = orderOpt.get();
            order.setStatus("CANCELLED");
            return databaseManager.update(order);
        }
        return false;
    }

    @Override
    public Optional<Order> getOrder(Long orderId) {
        return databaseManager.findById(Order.class, orderId);
    }

    @Override
    public List<Order> getUserOrders(Long userId) {
        List<Order> allOrders = databaseManager.findAll(Order.class);
        return allOrders.stream()
                .filter(order -> order.getUserId().equals(userId))
                .collect(Collectors.toList());
    }
}