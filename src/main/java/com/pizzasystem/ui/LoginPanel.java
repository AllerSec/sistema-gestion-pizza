package com.pizzasystem.ui;

import com.pizzasystem.di.DependencyInjector;
import com.pizzasystem.interfaces.IAuthenticator;
import com.pizzasystem.models.User;
import javax.swing.*;
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

        // Panel central con formulario
        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);

        // Título
        JLabel titleLabel = new JLabel("Iniciar Sesión", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        formPanel.add(titleLabel, gbc);

        // Campos de usuario y contraseña
        JLabel usernameLabel = new JLabel("Usuario:");
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        formPanel.add(usernameLabel, gbc);

        usernameField = new JTextField(20);
        gbc.gridx = 1;
        gbc.gridy = 1;
        formPanel.add(usernameField, gbc);

        JLabel passwordLabel = new JLabel("Contraseña:");
        gbc.gridx = 0;
        gbc.gridy = 2;
        formPanel.add(passwordLabel, gbc);

        passwordField = new JPasswordField(20);
        gbc.gridx = 1;
        gbc.gridy = 2;
        formPanel.add(passwordField, gbc);

        // Botones
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        loginButton = new JButton("Iniciar Sesión");
        registerButton = new JButton("Registrarse");

        buttonPanel.add(loginButton);
        buttonPanel.add(registerButton);

        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        formPanel.add(buttonPanel, gbc);

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
        JDialog registerDialog = new JDialog(SwingUtilities.getWindowAncestor(this), "Registro de Usuario", true);
        registerDialog.setLayout(new BorderLayout());
        registerDialog.setSize(400, 350);
        registerDialog.setLocationRelativeTo(this);

        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);

        // Campos de registro
        gbc.gridx = 0;
        gbc.gridy = 0;
        formPanel.add(new JLabel("Nombre de usuario:"), gbc);

        final JTextField usernameField = new JTextField(20);
        gbc.gridx = 1;
        formPanel.add(usernameField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        formPanel.add(new JLabel("Contraseña:"), gbc);

        final JPasswordField passwordField = new JPasswordField(20);
        gbc.gridx = 1;
        formPanel.add(passwordField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        formPanel.add(new JLabel("Nombre completo:"), gbc);

        final JTextField nameField = new JTextField(20);
        gbc.gridx = 1;
        formPanel.add(nameField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        formPanel.add(new JLabel("Email:"), gbc);

        final JTextField emailField = new JTextField(20);
        gbc.gridx = 1;
        formPanel.add(emailField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;
        formPanel.add(new JLabel("Dirección:"), gbc);

        final JTextField addressField = new JTextField(20);
        gbc.gridx = 1;
        formPanel.add(addressField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 5;
        formPanel.add(new JLabel("Teléfono:"), gbc);

        final JTextField phoneField = new JTextField(20);
        gbc.gridx = 1;
        formPanel.add(phoneField, gbc);

        JButton submitButton = new JButton("Registrarse");
        gbc.gridx = 0;
        gbc.gridy = 6;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        formPanel.add(submitButton, gbc);

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
                    JOptionPane.showMessageDialog(registerDialog,
                            "Todos los campos son obligatorios",
                            "Error",
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }

                // Crear y registrar usuario
                User newUser = new User(System.currentTimeMillis(), username, password, name, email, address, phone);

                if (authenticator.register(newUser)) {
                    JOptionPane.showMessageDialog(registerDialog,
                            "Usuario registrado correctamente",
                            "Registro exitoso",
                            JOptionPane.INFORMATION_MESSAGE);
                    registerDialog.dispose();
                } else {
                    JOptionPane.showMessageDialog(registerDialog,
                            "Error al registrar el usuario",
                            "Error",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        registerDialog.add(formPanel, BorderLayout.CENTER);
        registerDialog.setVisible(true);
    }
}