package com.pizzasystem.ui;

import com.pizzasystem.di.DependencyInjector;
import com.pizzasystem.interfaces.IAuthenticator;
import com.pizzasystem.interfaces.IOrderManager;
import com.pizzasystem.interfaces.IPaymentProcessor;
import com.pizzasystem.models.Order;
import com.pizzasystem.models.User;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.Optional;

public class PaymentPanel extends JPanel {
    private final MainFrame mainFrame;
    private final IAuthenticator authenticator;
    private final IOrderManager orderManager;
    private final IPaymentProcessor paymentProcessor;

    private JComboBox<String> paymentMethodCombo;
    private JTextField cardNumberField;
    private JTextField cardHolderField;
    private JTextField expiryField;
    private JTextField cvvField;
    private JLabel totalLabel;
    private JPanel cardPanel;

    public PaymentPanel(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        this.authenticator = DependencyInjector.getInstance().getAuthenticator();
        this.orderManager = DependencyInjector.getInstance().getOrderManager();
        this.paymentProcessor = DependencyInjector.getInstance().getPaymentProcessor();

        setLayout(new BorderLayout(10, 10));
        setBorder(new EmptyBorder(10, 10, 10, 10));
        setBackground(new Color(240, 240, 240));

        // Panel superior con título
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(70, 130, 180));
        headerPanel.setBorder(new EmptyBorder(10, 15, 10, 15));

        JLabel titleLabel = new JLabel("Finalizar Pedido", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        titleLabel.setForeground(Color.WHITE);
        headerPanel.add(titleLabel, BorderLayout.CENTER);

        // Botón para volver al pedido
        JButton backButton = new JButton("« Volver al Pedido");
        backButton.setBackground(new Color(70, 130, 180));
        backButton.setForeground(Color.WHITE);
        backButton.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        backButton.setFont(new Font("Arial", Font.BOLD, 12));
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mainFrame.showOrderPanel();
            }
        });
        headerPanel.add(backButton, BorderLayout.WEST);

        add(headerPanel, BorderLayout.NORTH);

        // Panel central con formulario de pago
        JPanel mainPanel = new JPanel(new BorderLayout(0, 20));
        mainPanel.setBackground(new Color(240, 240, 240));
        mainPanel.setBorder(new EmptyBorder(20, 20, 20, 20));

        // Panel de resumen de pedido
        JPanel summaryPanel = createSummaryPanel();
        mainPanel.add(summaryPanel, BorderLayout.NORTH);

        // Panel de método de pago
        JPanel paymentMethodPanel = createPaymentMethodPanel();
        mainPanel.add(paymentMethodPanel, BorderLayout.CENTER);

        add(mainPanel, BorderLayout.CENTER);

        // Panel inferior con botones
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        buttonPanel.setBackground(new Color(240, 240, 240));

        JButton confirmButton = new JButton("Confirmar Pago");
        confirmButton.setBackground(new Color(46, 204, 113));
        confirmButton.setForeground(Color.WHITE);
        confirmButton.setFont(new Font("Arial", Font.BOLD, 14));
        confirmButton.setPreferredSize(new Dimension(180, 40));
        confirmButton.setBorder(BorderFactory.createRaisedBevelBorder());
        confirmButton.addActionListener(e -> processPayment());

        JButton cancelButton = new JButton("Cancelar");
        cancelButton.setBackground(new Color(231, 76, 60));
        cancelButton.setForeground(Color.WHITE);
        cancelButton.setFont(new Font("Arial", Font.BOLD, 14));
        cancelButton.setPreferredSize(new Dimension(180, 40));
        cancelButton.setBorder(BorderFactory.createRaisedBevelBorder());
        cancelButton.addActionListener(e -> mainFrame.showOrderPanel());

        buttonPanel.add(confirmButton);
        buttonPanel.add(cancelButton);

        add(buttonPanel, BorderLayout.SOUTH);

        // Actualizar datos al mostrar el panel
        updateOrderTotal();
    }

    private JPanel createSummaryPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createCompoundBorder(
                new EmptyBorder(0, 0, 10, 0),
                BorderFactory.createTitledBorder(
                        BorderFactory.createLineBorder(new Color(70, 130, 180), 2),
                        "Resumen del Pedido",
                        TitledBorder.LEFT,
                        TitledBorder.TOP,
                        new Font("Arial", Font.BOLD, 14),
                        new Color(70, 130, 180)
                )
        ));
        panel.setBackground(new Color(240, 240, 240));

        // Obtener información del usuario
        String userName = "Cliente";
        String userAddress = "Dirección no disponible";

        Optional<User> userOpt = authenticator.getCurrentUser();
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            userName = user.getName();
            userAddress = user.getAddress();
        }

        // Panel de información
        JPanel infoPanel = new JPanel(new GridLayout(3, 1, 5, 5));
        infoPanel.setBackground(new Color(240, 240, 240));
        infoPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

        JLabel customerLabel = new JLabel("Cliente: " + userName);
        customerLabel.setFont(new Font("Arial", Font.BOLD, 14));

        JLabel addressLabel = new JLabel("Dirección de entrega: " + userAddress);
        addressLabel.setFont(new Font("Arial", Font.PLAIN, 14));

        totalLabel = new JLabel("Total a pagar: 0.00 €");
        totalLabel.setFont(new Font("Arial", Font.BOLD, 16));
        totalLabel.setForeground(new Color(211, 84, 0));

        infoPanel.add(customerLabel);
        infoPanel.add(addressLabel);
        infoPanel.add(totalLabel);

        panel.add(infoPanel, BorderLayout.CENTER);

        return panel;
    }

    private JPanel createPaymentMethodPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createCompoundBorder(
                new EmptyBorder(0, 0, 0, 0),
                BorderFactory.createTitledBorder(
                        BorderFactory.createLineBorder(new Color(70, 130, 180), 2),
                        "Método de Pago",
                        TitledBorder.LEFT,
                        TitledBorder.TOP,
                        new Font("Arial", Font.BOLD, 14),
                        new Color(70, 130, 180)
                )
        ));
        panel.setBackground(new Color(240, 240, 240));

        // Panel para selección de método
        JPanel methodPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        methodPanel.setBackground(new Color(240, 240, 240));

        JLabel methodLabel = new JLabel("Seleccione método de pago:");
        methodLabel.setFont(new Font("Arial", Font.BOLD, 14));

        paymentMethodCombo = new JComboBox<>(new String[]{"Tarjeta de crédito", "PayPal", "Efectivo"});
        paymentMethodCombo.setFont(new Font("Arial", Font.PLAIN, 14));
        paymentMethodCombo.setPreferredSize(new Dimension(200, 30));
        paymentMethodCombo.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updatePaymentMethodFields();
            }
        });

        methodPanel.add(methodLabel);
        methodPanel.add(paymentMethodCombo);

        panel.add(methodPanel, BorderLayout.NORTH);

        // Panel para campos de tarjeta
        cardPanel = new JPanel(new GridBagLayout());
        cardPanel.setBackground(new Color(240, 240, 240));
        cardPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);

        // Campos para tarjeta de crédito
        gbc.gridwidth = 1;
        gbc.gridx = 0;
        gbc.gridy = 0;
        JLabel cardNumberLabel = new JLabel("Número de tarjeta:");
        cardNumberLabel.setFont(new Font("Arial", Font.BOLD, 12));
        cardPanel.add(cardNumberLabel, gbc);

        cardNumberField = new JTextField(20);
        cardNumberField.setPreferredSize(new Dimension(200, 30));
        gbc.gridx = 1;
        gbc.gridwidth = 3;
        cardPanel.add(cardNumberField, gbc);

        gbc.gridwidth = 1;
        gbc.gridx = 0;
        gbc.gridy = 1;
        JLabel cardHolderLabel = new JLabel("Titular:");
        cardHolderLabel.setFont(new Font("Arial", Font.BOLD, 12));
        cardPanel.add(cardHolderLabel, gbc);

        cardHolderField = new JTextField(20);
        cardHolderField.setPreferredSize(new Dimension(200, 30));
        gbc.gridx = 1;
        gbc.gridwidth = 3;
        cardPanel.add(cardHolderField, gbc);

        gbc.gridwidth = 1;
        gbc.gridx = 0;
        gbc.gridy = 2;
        JLabel expiryLabel = new JLabel("Fecha exp. (MM/YY):");
        expiryLabel.setFont(new Font("Arial", Font.BOLD, 12));
        cardPanel.add(expiryLabel, gbc);

        expiryField = new JTextField(5);
        expiryField.setPreferredSize(new Dimension(80, 30));
        gbc.gridx = 1;
        cardPanel.add(expiryField, gbc);

        gbc.gridx = 2;
        JLabel cvvLabel = new JLabel("CVV:");
        cvvLabel.setFont(new Font("Arial", Font.BOLD, 12));
        cardPanel.add(cvvLabel, gbc);

        cvvField = new JTextField(3);
        cvvField.setPreferredSize(new Dimension(60, 30));
        gbc.gridx = 3;
        cardPanel.add(cvvField, gbc);

        panel.add(cardPanel, BorderLayout.CENTER);

        return panel;
    }

    private void updatePaymentMethodFields() {
        String selectedMethod = (String) paymentMethodCombo.getSelectedItem();

        // Mostrar u ocultar campos según el método seleccionado
        cardPanel.setVisible("Tarjeta de crédito".equals(selectedMethod));
        revalidate();
        repaint();
    }

    private void updateOrderTotal() {
        Optional<User> userOpt = authenticator.getCurrentUser();
        if (userOpt.isPresent()) {
            Long userId = userOpt.get().getId();
            List<Order> userOrders = orderManager.getUserOrders(userId);

            if (!userOrders.isEmpty()) {
                // Tomar el último pedido (más reciente)
                Order latestOrder = userOrders.get(userOrders.size() - 1);
                totalLabel.setText(String.format("Total a pagar: %.2f €", latestOrder.getTotalAmount()));
            }
        }

        // Actualizar campos de método de pago
        updatePaymentMethodFields();
    }

    private void processPayment() {
        String paymentMethod = (String) paymentMethodCombo.getSelectedItem();

        // Para tarjeta, validar campos
        if ("Tarjeta de crédito".equals(paymentMethod)) {
            if (cardNumberField.getText().isEmpty() ||
                    cardHolderField.getText().isEmpty() ||
                    expiryField.getText().isEmpty() ||
                    cvvField.getText().isEmpty()) {

                JOptionPane.showMessageDialog(this,
                        "Por favor complete todos los campos de la tarjeta",
                        "Campos incompletos",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }
        }

        Optional<User> userOpt = authenticator.getCurrentUser();
        if (userOpt.isPresent()) {
            Long userId = userOpt.get().getId();
            List<Order> userOrders = orderManager.getUserOrders(userId);

            if (!userOrders.isEmpty()) {
                Order latestOrder = userOrders.get(userOrders.size() - 1);

                // Construir detalles de pago
                StringBuilder paymentDetails = new StringBuilder();
                if ("Tarjeta de crédito".equals(paymentMethod)) {
                    paymentDetails.append("Tarjeta: ").append(maskCardNumber(cardNumberField.getText()));
                    paymentDetails.append(", Titular: ").append(cardHolderField.getText());
                } else {
                    paymentDetails.append(paymentMethod);
                }

                // Procesar pago
                if (paymentProcessor.processPayment(latestOrder, paymentMethod, paymentDetails.toString())) {
                    JOptionPane.showMessageDialog(this,
                            "¡Pago realizado con éxito!\n\nSu pedido está en camino.\n" +
                                    "Tiempo estimado de entrega: 30-45 minutos.",
                            "Pago completado",
                            JOptionPane.INFORMATION_MESSAGE);

                    // Volver al panel de pedidos
                    mainFrame.showOrderPanel();
                } else {
                    JOptionPane.showMessageDialog(this,
                            "Error al procesar el pago. Inténtelo de nuevo.",
                            "Error",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }

    private String maskCardNumber(String cardNumber) {
        // Mostrar solo los últimos 4 dígitos
        if (cardNumber.length() > 4) {
            return "****-****-****-" + cardNumber.substring(cardNumber.length() - 4);
        }
        return cardNumber;
    }
}

