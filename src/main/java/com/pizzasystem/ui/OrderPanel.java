package com.pizzasystem.ui;

import com.pizzasystem.di.DependencyInjector;
import com.pizzasystem.interfaces.IAuthenticator;
import com.pizzasystem.interfaces.IOrderManager;
import com.pizzasystem.models.Order;
import com.pizzasystem.models.Pizza;
import com.pizzasystem.models.User;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
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

        setLayout(new BorderLayout(10, 10));
        setBorder(new EmptyBorder(10, 10, 10, 10));
        setBackground(new Color(240, 240, 240));

        // Panel superior con información del usuario
        JPanel userPanel = createUserPanel();
        add(userPanel, BorderLayout.NORTH);

        // Panel central dividido en 2 partes: disponibles y carrito
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        splitPane.setResizeWeight(0.6);
        splitPane.setBorder(null);
        splitPane.setDividerSize(8);

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

        // Pizzas Margarita
        availablePizzas.add(new Pizza(1L, "Margarita", "Pequeña",
                Arrays.asList("Queso", "Tomate", "Albahaca"), 8.99));
        availablePizzas.add(new Pizza(2L, "Margarita", "Mediana",
                Arrays.asList("Queso", "Tomate", "Albahaca"), 10.99));
        availablePizzas.add(new Pizza(3L, "Margarita", "Grande",
                Arrays.asList("Queso", "Tomate", "Albahaca"), 12.99));

        // Pizzas Pepperoni
        availablePizzas.add(new Pizza(4L, "Pepperoni", "Pequeña",
                Arrays.asList("Pepperoni", "Queso", "Tomate"), 9.99));
        availablePizzas.add(new Pizza(5L, "Pepperoni", "Mediana",
                Arrays.asList("Pepperoni", "Queso", "Tomate"), 12.99));
        availablePizzas.add(new Pizza(6L, "Pepperoni", "Grande",
                Arrays.asList("Pepperoni", "Queso", "Tomate"), 14.99));

        // Pizzas Vegetarianas
        availablePizzas.add(new Pizza(7L, "Vegetariana", "Pequeña",
                Arrays.asList("Pimiento", "Cebolla", "Champiñones", "Queso", "Tomate"), 9.49));
        availablePizzas.add(new Pizza(8L, "Vegetariana", "Mediana",
                Arrays.asList("Pimiento", "Cebolla", "Champiñones", "Queso", "Tomate"), 11.49));
        availablePizzas.add(new Pizza(9L, "Vegetariana", "Grande",
                Arrays.asList("Pimiento", "Cebolla", "Champiñones", "Queso", "Tomate"), 13.49));

        // Pizzas Hawaiana
        availablePizzas.add(new Pizza(10L, "Hawaiana", "Pequeña",
                Arrays.asList("Jamón", "Piña", "Queso", "Tomate"), 10.49));
        availablePizzas.add(new Pizza(11L, "Hawaiana", "Mediana",
                Arrays.asList("Jamón", "Piña", "Queso", "Tomate"), 12.49));
        availablePizzas.add(new Pizza(12L, "Hawaiana", "Grande",
                Arrays.asList("Jamón", "Piña", "Queso", "Tomate"), 14.49));
    }

    private JPanel createUserPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(70, 130, 180));
        panel.setBorder(new EmptyBorder(10, 15, 10, 15));

        String username = "Usuario";
        Optional<User> userOpt = authenticator.getCurrentUser();
        if (userOpt.isPresent()) {
            username = userOpt.get().getName();
        }

        JLabel welcomeLabel = new JLabel("Bienvenido, " + username);
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 16));
        welcomeLabel.setForeground(Color.WHITE);
        panel.add(welcomeLabel, BorderLayout.WEST);

        JButton logoutButton = new JButton("Cerrar Sesión");
        logoutButton.setBackground(new Color(211, 84, 0));
        logoutButton.setForeground(Color.WHITE);
        logoutButton.setFont(new Font("Arial", Font.BOLD, 12));
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
        JPanel panel = new JPanel(new BorderLayout(0, 10));
        panel.setBorder(BorderFactory.createCompoundBorder(
                new EmptyBorder(5, 5, 5, 5),
                BorderFactory.createTitledBorder(
                        BorderFactory.createLineBorder(new Color(70, 130, 180), 2),
                        "Menú de Pizzas",
                        TitledBorder.LEFT,
                        TitledBorder.TOP,
                        new Font("Arial", Font.BOLD, 14),
                        new Color(70, 130, 180)
                )
        ));
        panel.setBackground(new Color(240, 240, 240));

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
        pizzaTable.setRowHeight(25);
        pizzaTable.setFont(new Font("Arial", Font.PLAIN, 12));
        pizzaTable.setShowGrid(true);
        pizzaTable.setGridColor(new Color(220, 220, 220));

        JTableHeader header = pizzaTable.getTableHeader();
        header.setFont(new Font("Arial", Font.BOLD, 12));
        header.setBackground(new Color(70, 130, 180));
        header.setForeground(Color.WHITE);

        JScrollPane scrollPane = new JScrollPane(pizzaTable);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        panel.add(scrollPane, BorderLayout.CENTER);

        JButton addButton = new JButton("Añadir al Carrito");
        addButton.setBackground(new Color(46, 204, 113));
        addButton.setForeground(Color.WHITE);
        addButton.setFont(new Font("Arial", Font.BOLD, 14));
        addButton.setPreferredSize(new Dimension(150, 40));
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = pizzaTable.getSelectedRow();
                if (selectedRow >= 0) {
                    Pizza selectedPizza = availablePizzas.get(selectedRow);
                    cartPizzas.add(selectedPizza);
                    updateCartTable();
                } else {
                    JOptionPane.showMessageDialog(OrderPanel.this,
                            "Por favor, seleccione una pizza del menú",
                            "Selección requerida",
                            JOptionPane.INFORMATION_MESSAGE);
                }
            }
        });

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.setBackground(new Color(240, 240, 240));
        buttonPanel.add(addButton);
        panel.add(buttonPanel, BorderLayout.SOUTH);

        return panel;
    }

    private JPanel createCartPanel() {
        JPanel panel = new JPanel(new BorderLayout(0, 10));
        panel.setBorder(BorderFactory.createCompoundBorder(
                new EmptyBorder(5, 5, 5, 5),
                BorderFactory.createTitledBorder(
                        BorderFactory.createLineBorder(new Color(211, 84, 0), 2),
                        "Tu Pedido",
                        TitledBorder.LEFT,
                        TitledBorder.TOP,
                        new Font("Arial", Font.BOLD, 14),
                        new Color(211, 84, 0)
                )
        ));
        panel.setBackground(new Color(240, 240, 240));

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
        cartTable.setRowHeight(25);
        cartTable.setFont(new Font("Arial", Font.PLAIN, 12));
        cartTable.setShowGrid(true);
        cartTable.setGridColor(new Color(220, 220, 220));

        JTableHeader header = cartTable.getTableHeader();
        header.setFont(new Font("Arial", Font.BOLD, 12));
        header.setBackground(new Color(211, 84, 0));
        header.setForeground(Color.WHITE);

        header.setForeground(Color.WHITE);

        JScrollPane scrollPane = new JScrollPane(cartTable);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        panel.add(scrollPane, BorderLayout.CENTER);

        JPanel southPanel = new JPanel(new BorderLayout(10, 0));
        southPanel.setBackground(new Color(240, 240, 240));
        southPanel.setBorder(new EmptyBorder(5, 5, 5, 5));

        totalLabel = new JLabel("Total: 0.00 €");
        totalLabel.setFont(new Font("Arial", Font.BOLD, 16));
        totalLabel.setForeground(new Color(211, 84, 0));
        southPanel.add(totalLabel, BorderLayout.WEST);

        JButton removeButton = new JButton("Quitar");
        removeButton.setBackground(new Color(231, 76, 60));
        removeButton.setForeground(Color.WHITE);
        removeButton.setFont(new Font("Arial", Font.BOLD, 12));
        removeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = cartTable.getSelectedRow();
                if (selectedRow >= 0 && selectedRow < cartPizzas.size()) {
                    cartPizzas.remove(selectedRow);
                    updateCartTable();
                } else if (cartPizzas.size() > 0) {
                    JOptionPane.showMessageDialog(OrderPanel.this,
                            "Por favor, seleccione una pizza para quitar",
                            "Selección requerida",
                            JOptionPane.INFORMATION_MESSAGE);
                }
            }
        });
        southPanel.add(removeButton, BorderLayout.EAST);

        panel.add(southPanel, BorderLayout.SOUTH);
        return panel;
    }

    private JPanel createActionPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 20, 10));
        panel.setBackground(new Color(240, 240, 240));
        panel.setBorder(new EmptyBorder(10, 10, 10, 10));

        JButton clearButton = new JButton("Vaciar Carrito");
        clearButton.setBackground(new Color(231, 76, 60));
        clearButton.setForeground(Color.WHITE);
        clearButton.setFont(new Font("Arial", Font.BOLD, 14));
        clearButton.setPreferredSize(new Dimension(150, 40));
        clearButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (cartPizzas.size() > 0) {
                    int option = JOptionPane.showConfirmDialog(OrderPanel.this,
                            "¿Está seguro de que desea vaciar el carrito?",
                            "Confirmar acción",
                            JOptionPane.YES_NO_OPTION);

                    if (option == JOptionPane.YES_OPTION) {
                        cartPizzas.clear();
                        updateCartTable();
                    }
                } else {
                    JOptionPane.showMessageDialog(OrderPanel.this,
                            "El carrito ya está vacío",
                            "Información",
                            JOptionPane.INFORMATION_MESSAGE);
                }
            }
        });

        JButton checkoutButton = new JButton("Realizar Pedido");
        checkoutButton.setBackground(new Color(46, 204, 113));
        checkoutButton.setForeground(Color.WHITE);
        checkoutButton.setFont(new Font("Arial", Font.BOLD, 14));
        checkoutButton.setPreferredSize(new Dimension(150, 40));
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