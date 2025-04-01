package com.pizzasystem.ui;

import com.pizzasystem.di.DependencyInjector;
import com.pizzasystem.interfaces.IAuthenticator;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MainFrame extends JFrame {
    private CardLayout cardLayout;
    private JPanel mainPanel;
    private LoginPanel loginPanel;
    private OrderPanel orderPanel;
    private PaymentPanel paymentPanel;
    private JMenuBar menuBar;

    private final IAuthenticator authenticator;

    public MainFrame() {
        // Obtener dependencias
        DependencyInjector injector = DependencyInjector.getInstance();
        authenticator = injector.getAuthenticator();

        // Configurar ventana principal
        setTitle("Sistema de Pedidos de Pizza");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(900, 650);
        setLocationRelativeTo(null);

        // Crear menú principal
        createMenuBar();

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

        // Aplicar tema visual mejorado
        applyVisualImprovements();
    }

    private void createMenuBar() {
        menuBar = new JMenuBar();

        // Menú Archivo
        JMenu fileMenu = new JMenu("Archivo");
        JMenuItem exitItem = new JMenuItem("Salir");
        exitItem.addActionListener(e -> System.exit(0));
        fileMenu.add(exitItem);

        // Menú Navegación
        JMenu navMenu = new JMenu("Navegación");

        JMenuItem loginItem = new JMenuItem("Iniciar Sesión");
        loginItem.addActionListener(e -> showLoginPanel());

        JMenuItem orderItem = new JMenuItem("Realizar Pedido");
        orderItem.addActionListener(e -> {
            if (authenticator.getCurrentUser().isPresent()) {
                showOrderPanel();
            } else {
                JOptionPane.showMessageDialog(this,
                        "Debe iniciar sesión primero",
                        "Acceso denegado",
                        JOptionPane.WARNING_MESSAGE);
            }
        });

        navMenu.add(loginItem);
        navMenu.add(orderItem);

        // Menú Ayuda
        JMenu helpMenu = new JMenu("Ayuda");
        JMenuItem aboutItem = new JMenuItem("Acerca de");
        aboutItem.addActionListener(e ->
                JOptionPane.showMessageDialog(this,
                        "Sistema de Pedidos de Pizza v1.0\n" +
                                "Desarrollado como ejemplo de aplicación SOLID\n" +
                                "con inyección de dependencias",
                        "Acerca de",
                        JOptionPane.INFORMATION_MESSAGE)
        );
        helpMenu.add(aboutItem);

        // Agregar menús a la barra
        menuBar.add(fileMenu);
        menuBar.add(navMenu);
        menuBar.add(helpMenu);

        // Establecer la barra de menú
        setJMenuBar(menuBar);
    }

    private void applyVisualImprovements() {
        // Establecer fuentes y colores para mejorar la apariencia
        // IMPORTANTE: No establecer colores globales para botones para evitar conflictos
        UIManager.put("Label.font", new Font("Arial", Font.PLAIN, 12));
        UIManager.put("TextField.font", new Font("Arial", Font.PLAIN, 12));
        UIManager.put("Table.font", new Font("Arial", Font.PLAIN, 12));
        UIManager.put("TableHeader.font", new Font("Arial", Font.BOLD, 12));
        UIManager.put("Menu.font", new Font("Arial", Font.BOLD, 12));
        UIManager.put("MenuItem.font", new Font("Arial", Font.PLAIN, 12));

        // Asegurar que los botones tengan buen contraste
        UIManager.put("Button.font", new Font("Arial", Font.BOLD, 12));
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
