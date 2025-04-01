package com.pizzasystem;

import com.pizzasystem.di.DependencyInjector;
import com.pizzasystem.models.User;
import com.pizzasystem.ui.MainFrame;

import javax.swing.*;
import java.awt.*;

public class App {
    public static void main(String[] args) {
        // Configurar look and feel
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Inicializar el inyector de dependencias
        final DependencyInjector injector = DependencyInjector.getInstance();

        // Crear datos de ejemplo (usuarios de prueba)
        createSampleData();

        // Iniciar la interfaz gráfica en el EDT
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                MainFrame mainFrame = new MainFrame();
                mainFrame.setVisible(true);
            }
        });

        // Agregar hook para cerrar recursos al salir
        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                injector.shutdown();
            }
        });
    }

    private static void createSampleData() {
        DependencyInjector injector = DependencyInjector.getInstance();

        // Crear usuario de prueba
        User testUser = new User(1L, "usuario", "password", "Usuario de Prueba",
                "usuario@ejemplo.com", "Calle Principal 123", "555-123456");

        // Registrar usuario en el sistema
        injector.getAuthenticator().register(testUser);
    }
}