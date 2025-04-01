package com.pizzasystem.ui;

import com.pizzasystem.di.DependencyInjector;
import com.pizzasystem.interfaces.IAuthenticator;
import com.pizzasystem.models.User;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LoginPanel extends JPanel {
    private final MainFrame mainFrame;
    private final IAuthenticator authenticator;

    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private JButton registerButton;

    public LoginPanel(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        this.authenticator = DependencyInjector.getInstance().getAuthenticator();

        setLayout(new BorderLayout());
        setBackground(new Color(240, 240, 240));

        // Panel de logo/banner
        JPanel bannerPanel = new JPanel();
        bannerPanel.setBackground(new Color(70, 130, 180));
        bannerPanel.setPreferredSize(new Dimension(600, 100));
        JLabel bannerLabel = new JLabel("PIZZA DELIVERY SYSTEM");
        bannerLabel.setFont(new Font("Arial", Font.BOLD, 28));
        bannerLabel.setForeground(Color.WHITE);
        bannerPanel.add(bannerLabel);
        add(bannerPanel, BorderLayout.NORTH);

        // Panel central con formulario
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(new Color(240, 240, 240));
        formPanel.setBorder(new EmptyBorder(20, 40, 20, 40));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(8, 8, 8, 8);

        // Título
        JLabel titleLabel = new JLabel("Iniciar Sesión", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(new Color(70, 130, 180));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        formPanel.add(titleLabel, gbc);

        // Campos de usuario y contraseña
        JLabel usernameLabel = new JLabel("Usuario:");
        usernameLabel.setFont(new Font("Arial", Font.BOLD, 14));
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        formPanel.add(usernameLabel, gbc);

        usernameField = new JTextField(20);
        usernameField.setPreferredSize(new Dimension(200, 30));
        gbc.gridx = 1;
        gbc.gridy = 1;
        formPanel.add(usernameField, gbc);

        JLabel passwordLabel = new JLabel("Contraseña:");
        passwordLabel.setFont(new Font("Arial", Font.BOLD, 14));
        gbc.gridx = 0;
        gbc.gridy = 2;
        formPanel.add(passwordLabel, gbc);

        passwordField = new JPasswordField(20);
        passwordField.setPreferredSize(new Dimension(200, 30));
        gbc.gridx = 1;
        gbc.gridy = 2;
        formPanel.add(passwordField, gbc);

        // Botones
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        buttonPanel.setBackground(new Color(240, 240, 240));

        loginButton = new JButton("Iniciar Sesión");
        loginButton.setPreferredSize(new Dimension(150, 40));
        loginButton.setBackground(new Color(70, 130, 180));
        loginButton.setForeground(Color.WHITE);
        loginButton.setFont(new Font("Arial", Font.BOLD, 14));

        registerButton = new JButton("Registrarse");
        registerButton.setPreferredSize(new Dimension(150, 40));
        registerButton.setBackground(new Color(60, 179, 113));
        registerButton.setForeground(Color.WHITE);
        registerButton.setFont(new Font("Arial", Font.BOLD, 14));

        buttonPanel.add(loginButton);
        buttonPanel.add(registerButton);

        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(20, 8, 8, 8);
        formPanel.add(buttonPanel, gbc);

        // Información de usuario de prueba
        JPanel infoPanel = new JPanel();
        infoPanel.setBackground(new Color(240, 240, 240));
        JLabel infoLabel = new JLabel("<html><body>Usuario de prueba:<br>usuario / password</body></html>");
        infoLabel.setFont(new Font("Arial", Font.ITALIC, 12));
        infoLabel.setForeground(Color.GRAY);
        infoPanel.add(infoLabel);

        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(20, 8, 8, 8);
        formPanel.add(infoPanel, gbc);

        // Agregar panel de formulario al centro
        add(formPanel, BorderLayout.CENTER);

        // Configurar eventos
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = usernameField.getText();
                String password = new String(passwordField.getPassword());

                if (authenticator.login(username, password)) {
                    JOptionPane.showMessageDialog(LoginPanel.this,
                            "¡Bienvenido, " + username + "!",
                            "Login exitoso",
                            JOptionPane.INFORMATION_MESSAGE);
                    mainFrame.showOrderPanel();
                } else {
                    JOptionPane.showMessageDialog(LoginPanel.this,
                            "Usuario o contraseña incorrectos",
                            "Error de autenticación",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showRegisterDialog();
            }
        });
    }

    private void showRegisterDialog() {
        // Crear un nuevo JFrame en lugar de un JDialog
        JFrame registerFrame = new JFrame("Registro de Usuario");
        registerFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        registerFrame.setSize(450, 400);
        registerFrame.setLocationRelativeTo(this);
        registerFrame.setLayout(new BorderLayout());

        // Panel de título
        JPanel titlePanel = new JPanel();
        titlePanel.setBackground(new Color(70, 130, 180));
        titlePanel.setPreferredSize(new Dimension(450, 60));
        JLabel titleLabel = new JLabel("REGISTRO DE USUARIO");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        titleLabel.setForeground(Color.WHITE);
        titlePanel.add(titleLabel);
        registerFrame.add(titlePanel, BorderLayout.NORTH);

        // Panel principal con GridLayout más simple
        JPanel mainPanel = new JPanel(new GridLayout(7, 2, 10, 10));
        mainPanel.setBorder(new EmptyBorder(15, 20, 15, 20));

        // Crear etiquetas y campos
        JLabel usernameLabel = new JLabel("Nombre de usuario:");
        JTextField usernameField = new JTextField(20);

        JLabel passwordLabel = new JLabel("Contraseña:");
        JPasswordField passwordField = new JPasswordField(20);

        JLabel nameLabel = new JLabel("Nombre completo:");
        JTextField nameField = new JTextField(20);

        JLabel emailLabel = new JLabel("Email:");
        JTextField emailField = new JTextField(20);

        JLabel addressLabel = new JLabel("Dirección:");
        JTextField addressField = new JTextField(20);

        JLabel phoneLabel = new JLabel("Teléfono:");
        JTextField phoneField = new JTextField(20);

        // Añadir componentes al panel
        mainPanel.add(usernameLabel);
        mainPanel.add(usernameField);
        mainPanel.add(passwordLabel);
        mainPanel.add(passwordField);
        mainPanel.add(nameLabel);
        mainPanel.add(nameField);
        mainPanel.add(emailLabel);
        mainPanel.add(emailField);
        mainPanel.add(addressLabel);
        mainPanel.add(addressField);
        mainPanel.add(phoneLabel);
        mainPanel.add(phoneField);

        // Botón de registro
        JButton submitButton = new JButton("Registrarse");
        submitButton.setFocusable(true);
        submitButton.setBackground(new Color(60, 179, 113));
        submitButton.setForeground(Color.WHITE);
        submitButton.setFont(new Font("Arial", Font.BOLD, 14));

        // Panel para botón
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(submitButton);

        // Añadir paneles al frame
        registerFrame.add(mainPanel, BorderLayout.CENTER);
        registerFrame.add(buttonPanel, BorderLayout.SOUTH);

        // Acción del botón
        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = usernameField.getText();
                String password = new String(passwordField.getPassword());
                String name = nameField.getText();
                String email = emailField.getText();
                String address = addressField.getText();
                String phone = phoneField.getText();

                if (username.isEmpty() || password.isEmpty() || name.isEmpty() ||
                        email.isEmpty() || address.isEmpty() || phone.isEmpty()) {
                    JOptionPane.showMessageDialog(registerFrame,
                            "Todos los campos son obligatorios",
                            "Error",
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }

                // Crear y registrar usuario
                User newUser = new User(System.currentTimeMillis(), username, password, name, email, address, phone);

                if (authenticator.register(newUser)) {
                    JOptionPane.showMessageDialog(registerFrame,
                            "Usuario registrado correctamente",
                            "Registro exitoso",
                            JOptionPane.INFORMATION_MESSAGE);
                    registerFrame.dispose();
                } else {
                    JOptionPane.showMessageDialog(registerFrame,
                            "Error al registrar el usuario",
                            "Error",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        // Asegurar que los campos sean enfocables
        for (Component comp : mainPanel.getComponents()) {
            if (comp instanceof JTextField) {
                ((JTextField) comp).setFocusable(true);
                ((JTextField) comp).setEnabled(true);
            }
        }

        // Mostrar el frame
        registerFrame.setVisible(true);

        // Intentar darle foco al primer campo
        SwingUtilities.invokeLater(() -> usernameField.requestFocusInWindow());
    }
}