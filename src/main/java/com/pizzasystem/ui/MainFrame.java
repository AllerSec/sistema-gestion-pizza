package com.pizzasystem.ui;

import com.pizzasystem.di.DependencyInjector;
import com.pizzasystem.interfaces.IAuthenticator;
import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {
    private CardLayout cardLayout;
    private JPanel mainPanel;
    private LoginPanel loginPanel;
    private OrderPanel orderPanel;
    private PaymentPanel paymentPanel;

    private final IAuthenticator authenticator;

    public MainFrame() {
        // Obtener dependencias
        DependencyInjector injector = DependencyInjector.getInstance();
        authenticator = injector.getAuthenticator();

        // Configurar ventana principal
        setTitle("Sistema de Pedidos de Pizza");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);

        // Crear layout para navegar entre paneles
        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        // Inicializar paneles
        loginPanel = new LoginPanel(this);
        orderPanel = new OrderPanel(this);
        paymentPanel = new PaymentPanel(this);

        // Agregar paneles al layout
        mainPanel.add(loginPanel, "LOGIN");
        mainPanel.add(orderPanel, "ORDER");
        mainPanel.add(paymentPanel, "PAYMENT");

        // Mostrar el panel de login inicialmente
        cardLayout.show(mainPanel, "LOGIN");

        // Agregar el panel principal al frame
        getContentPane().add(mainPanel, BorderLayout.CENTER);
    }

    public void showLoginPanel() {
        cardLayout.show(mainPanel, "LOGIN");
    }

    public void showOrderPanel() {
        cardLayout.show(mainPanel, "ORDER");
        orderPanel.refreshData();
    }

    public void showPaymentPanel() {
        cardLayout.show(mainPanel, "PAYMENT");
    }

    public IAuthenticator getAuthenticator() {
        return authenticator;
    }
}