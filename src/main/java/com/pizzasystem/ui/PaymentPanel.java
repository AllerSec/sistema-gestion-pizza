package com.pizzasystem.ui;

import com.pizzasystem.di.DependencyInjector;
import com.pizzasystem.interfaces.IAuthenticator;
import com.pizzasystem.interfaces.IOrderManager;
import com.pizzasystem.interfaces.IPaymentProcessor;
import com.pizzasystem.models.Order;
import com.pizzasystem.models.User;

import javax.swing.*;
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

    public PaymentPanel(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        this.authenticator = DependencyInjector.getInstance().getAuthenticator();
        this.orderManager = DependencyInjector.getInstance().getOrderManager();
        this.paymentProcessor = DependencyInjector.getInstance().getPaymentProcessor();

        setLayout(new BorderLayout());

        // Panel superior con título
        JPanel headerPanel = new JPanel(new BorderLayout());
        JLabel titleLabel = new JLabel("Pago", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        headerPanel.add(titleLabel, BorderLayout.CENTER);

        // Botón para volver al pedido
        JButton backButton = new JButton("Volver al Pedido");
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mainFrame.showOrderPanel();
            }
        });
        headerPanel.add(backButton, BorderLayout.WEST);

        add(headerPanel, BorderLayout.NORTH);

        // Panel central con formulario de pago
        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);

        // Método de pago
        gbc.gridx = 0;
        gbc.gridy = 0;
        formPanel.add(new JLabel("Método de pago:"), gbc);

        paymentMethodCombo = new JComboBox<>(new String[]{"Tarjeta de crédito", "PayPal", "Efectivo"});
        gbc.gridx = 1;
        gbc.gridwidth = 2;
        formPanel.add(paymentMethodCombo, gbc);

        // Campos para tarjeta de crédito
        gbc.gridwidth = 1;
        gbc.gridx = 0;
        gbc.gridy = 1;
        formPanel.add(new JLabel("Número de tarjeta:"), gbc);

        cardNumberField = new JTextField(20);
        gbc.gridx = 1;
        gbc.gridwidth = 2;
        formPanel.add(cardNumberField, gbc);

        gbc.gridwidth = 1;
        gbc.gridx = 0;
        gbc.gridy = 2;
        formPanel.add(new JLabel("Titular:"), gbc);

        cardHolderField = new JTextField(20);
        gbc.gridx = 1;
        gbc.gridwidth = 2;
        formPanel.add(cardHolderField, gbc);

        gbc.gridwidth = 1;
        gbc.gridx = 0;
        gbc.gridy = 3;
        formPanel.add(new JLabel("Fecha exp. (MM/YY):"), gbc);

        expiryField = new JTextField(5);
        gbc.gridx = 1;
        formPanel.add(expiryField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;
        formPanel.add(new JLabel("CVV:"), gbc);

        cvvField = new JTextField(3);
        gbc.gridx = 1;
        formPanel.add(cvvField, gbc);

        // Total a pagar
        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.gridwidth = 1;
        formPanel.add(new JLabel("Total a pagar:"), gbc);

        totalLabel = new JLabel("0.00 €");
        totalLabel.setFont(new Font("Arial", Font.BOLD, 16));
        gbc.gridx = 1;
        formPanel.add(totalLabel, gbc);

        add(new JScrollPane(formPanel), BorderLayout.CENTER);

        // Panel inferior con botones
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));

        JButton confirmButton = new JButton("Confirmar Pago");
        confirmButton.addActionListener(e -> processPayment());

        JButton cancelButton = new JButton("Cancelar");
        cancelButton.addActionListener(e -> mainFrame.showOrderPanel());

        buttonPanel.add(confirmButton);
        buttonPanel.add(cancelButton);

        add(buttonPanel, BorderLayout.SOUTH);

        // Actualizar datos al mostrar el panel
        updateOrderTotal();
    }

    private void updateOrderTotal() {
        Optional<User> userOpt = authenticator.getCurrentUser();
        if (userOpt.isPresent()) {
            Long userId = userOpt.get().getId();
            List<Order> userOrders = orderManager.getUserOrders(userId);

            if (!userOrders.isEmpty()) {
                // Tomar el último pedido (más reciente)
                Order latestOrder = userOrders.get(userOrders.size() - 1);
                totalLabel.setText(String.format("%.2f €", latestOrder.getTotalAmount()));
            }
        }
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
                            "¡Pago realizado con éxito!\nSu pedido está en camino.",
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
