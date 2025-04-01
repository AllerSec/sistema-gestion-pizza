package com.pizzasystem.models;

import java.util.List;
import java.util.ArrayList;

public class Pizza {
    private Long id;
    private String name;
    private String size;
    private List<String> toppings;
    private double price;

    // Constructor
    public Pizza(Long id, String name, String size, List<String> toppings, double price) {
        this.id = id;
        this.name = name;
        this.size = size;
        this.toppings = toppings != null ? toppings : new ArrayList<>();
        this.price = price;
    }

    // Getters y setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getSize() { return size; }
    public void setSize(String size) { this.size = size; }

    public List<String> getToppings() { return toppings; }
    public void setToppings(List<String> toppings) { this.toppings = toppings; }

    public void addTopping(String topping) {
        if (toppings == null) {
            toppings = new ArrayList<>();
        }
        toppings.add(topping);
    }

    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }
}