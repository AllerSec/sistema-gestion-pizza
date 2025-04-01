package com.pizzasystem.interfaces;

import com.pizzasystem.models.Order;
import java.util.List;
import java.util.Optional;

public interface IOrderManager {
    boolean createOrder(Order order);
    boolean updateOrder(Order order);
    boolean cancelOrder(Long orderId);
    Optional<Order> getOrder(Long orderId);
    List<Order> getUserOrders(Long userId);
}