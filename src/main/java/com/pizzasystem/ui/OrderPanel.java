package com.pizzasystem.ui;

import com.pizzasystem.di.DependencyInjector;
import com.pizzasystem.interfaces.IAuthenticator;
import com.pizzasystem.interfaces.IOrderManager;
import com.pizzasystem.models.Order;
import com.pizzasystem.models.Pizza;
import com.pizzasystem.models.User;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class OrderPanel extends JPanel {
    private final MainFrame mainFrame;
    private final IAuthenticator authenticator;
    private final IOrderManager orderManager;

    private JTable pizzaTable;
    private DefaultTableModel pizzaTableModel;
    private JTable cartTable;
    private DefaultTableModel cartTableModel;
    private JLabel totalLabel;
    private List<Pizza> availablePizzas;
    private List<Pizza> cartPizzas;

    public OrderPanel(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        this.authenticator = DependencyInjector.getInstance().getAuthenticator();
        this.orderManager = DependencyInjector.getInstance().getOrderManager();
        this.cartPizzas = new ArrayList<>();

        // Inicializar pizzas disponibles (en una app real, se cargarían de la BD)
        initializePizzas();

        setLayout(new BorderLayout());

        // Panel superior con información del usuario
        JPanel userPanel = createUserPanel();
        add(userPanel, BorderLayout.NORTH);

        // Panel central dividido en 2 partes: disponibles y carrito
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        splitPane.setResizeWeight(0.5);

        // Panel izquierdo: Pizzas disponibles
        JPanel availablePanel = createAvailablePanel();
        splitPane.setLeftComponent(availablePanel);

        // Panel derecho: Carrito
        JPanel cartPanel = createCartPanel();
        splitPane.setRightComponent(cartPanel);

        add(splitPane, BorderLayout.CENTER);

        // Panel inferior con botones de acción
        JPanel actionPanel = createActionPanel();
        add(actionPanel, BorderLayout.SOUTH);
    }

    private void initializePizzas() {
        availablePizzas = new ArrayList<>();

        availablePizzas.add(new Pizza(1L, "Margarita", "Pequeña",
                Arrays.asList("Queso", "Tomate", "Albahaca"), 8.99));
        availablePizzas.add(new Pizza(2L, "Margarita", "Mediana",
                Arrays.asList("Queso", "Tomate", "Albahaca"), 10.99));
        availablePizzas.add(new Pizza(3L, "Margarita", "Grande",
                Arrays.asList("Queso", "Tomate", "Albahaca"), 12.99));

        availablePizzas.add(new Pizza(4L, "Pepperoni", "Pequeña",
                Arrays.asList("Pepperoni", "Queso", "Tomate"), 9.99));
        availablePizzas.add(new Pizza(5L, "Pepperoni", "Mediana",
                Arrays.asList("Pepperoni", "Queso", "Tomate"), 12.99));
        availablePizzas.add(new Pizza(6L, "Pepperoni", "Grande",
                Arrays.asList("Pepperoni", "Queso", "Tomate"), 14.99));

        availablePizzas.add(new Pizza(7L, "Vegetariana", "Pequeña",
                Arrays.asList("Pimiento", "Cebolla", "Champiñones", "Queso", "Tomate"), 9.49));
        availablePizzas.add(new Pizza(8L, "Vegetariana", "Mediana",
                Arrays.asList("Pimiento", "Cebolla", "Champiñones", "Queso", "Tomate"), 11.49));
        availablePizzas.add(new Pizza(9L, "Vegetariana", "Grande",
                Arrays.asList("Pimiento", "Cebolla", "Champiñones", "Queso", "Tomate"), 13.49));
    }

    private JPanel createUserPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        String username = "Usuario";
        Optional<User> userOpt = authenticator.getCurrentUser();
        if (userOpt.isPresent()) {
            username = userOpt.get().getName();
        }

        JLabel welcomeLabel = new JLabel("Bienvenido, " + username);
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 16));
        panel.add(welcomeLabel, BorderLayout.WEST);

        JButton logoutButton = new JButton("Cerrar Sesión");
        logoutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Optional<User> user = authenticator.getCurrentUser();
                if (user.isPresent()) {
                    authenticator.logout(user.get().getUsername());
                }
                mainFrame.showLoginPanel();
            }
        });
        panel.add(logoutButton, BorderLayout.EAST);

        return panel;
    }

    private JPanel createAvailablePanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Pizzas Disponibles"));

        // Crear modelo de tabla
        String[] columns = {"Nombre", "Tamaño", "Ingredientes", "Precio"};
        pizzaTableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        // Llenar tabla con datos
        for (Pizza pizza : availablePizzas) {
            Object[] row = {
                    pizza.getName(),
                    pizza.getSize(),
                    String.join(", ", pizza.getToppings()),
                    String.format("%.2f €", pizza.getPrice())
            };
            pizzaTableModel.addRow(row);
        }

        pizzaTable = new JTable(pizzaTableModel);
        pizzaTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JScrollPane scrollPane = new JScrollPane(pizzaTable);
        panel.add(scrollPane, BorderLayout.CENTER);

        JButton addButton = new JButton("Añadir al Carrito");
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = pizzaTable.getSelectedRow();
                if (selectedRow >= 0) {
                    Pizza selectedPizza = availablePizzas.get(selectedRow);
                    cartPizzas.add(selectedPizza);
                    updateCartTable();
                }
            }
        });

        panel.add(addButton, BorderLayout.SOUTH);
        return panel;
    }

    private JPanel createCartPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Carrito"));

        // Crear modelo de tabla
        String[] columns = {"Nombre", "Tamaño", "Precio"};
        cartTableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        cartTable = new JTable(cartTableModel);
        cartTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JScrollPane scrollPane = new JScrollPane(cartTable);
        panel.add(scrollPane, BorderLayout.CENTER);

        JPanel southPanel = new JPanel(new BorderLayout());

        totalLabel = new JLabel("Total: 0.00 €");
        totalLabel.setFont(new Font("Arial", Font.BOLD, 14));
        southPanel.add(totalLabel, BorderLayout.WEST);

        JButton removeButton = new JButton("Quitar");
        removeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = cartTable.getSelectedRow();
                if (selectedRow >= 0 && selectedRow < cartPizzas.size()) {
                    cartPizzas.remove(selectedRow);
                    updateCartTable();
                }
            }
        });
        southPanel.add(removeButton, BorderLayout.EAST);

        panel.add(southPanel, BorderLayout.SOUTH);
        return panel;
    }

    private JPanel createActionPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.RIGHT));

        JButton clearButton = new JButton("Vaciar Carrito");
        clearButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cartPizzas.clear();
                updateCartTable();
            }
        });

        JButton checkoutButton = new JButton("Realizar Pedido");
        checkoutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (cartPizzas.isEmpty()) {
                    JOptionPane.showMessageDialog(OrderPanel.this,
                            "El carrito está vacío",
                            "No se puede realizar el pedido",
                            JOptionPane.WARNING_MESSAGE);
                    return;
                }

                Optional<User> userOpt = authenticator.getCurrentUser();
                if (!userOpt.isPresent()) {
                    JOptionPane.showMessageDialog(OrderPanel.this,
                            "Debe iniciar sesión para realizar un pedido",
                            "Error",
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }

                // Crear pedido
                Order newOrder = new Order(System.currentTimeMillis(), userOpt.get().getId());
                for (Pizza pizza : cartPizzas) {
                    newOrder.addPizza(pizza);
                }

                // Calcular total
                double total = calculateTotal();
                newOrder.setTotalAmount(total);

                // Guardar pedido en el DI
                orderManager.createOrder(newOrder);

                // Mostrar panel de pago
                mainFrame.showPaymentPanel();
            }
        });

        panel.add(clearButton);
        panel.add(checkoutButton);

        return panel;
    }

    private void updateCartTable() {
        // Limpiar tabla
        cartTableModel.setRowCount(0);

        // Añadir pizzas del carrito
        for (Pizza pizza : cartPizzas) {
            Object[] row = {
                    pizza.getName(),
                    pizza.getSize(),
                    String.format("%.2f €", pizza.getPrice())
            };
            cartTableModel.addRow(row);
        }

        // Actualizar total
        double total = calculateTotal();
        totalLabel.setText(String.format("Total: %.2f €", total));
    }

    private double calculateTotal() {
        double total = 0;
        for (Pizza pizza : cartPizzas) {
            total += pizza.getPrice();
        }
        return total;
    }

    public void refreshData() {
        // Método para actualizar datos cuando se muestra el panel
        Optional<User> userOpt = authenticator.getCurrentUser();
        if (!userOpt.isPresent()) {
            mainFrame.showLoginPanel();
        }
    }
}