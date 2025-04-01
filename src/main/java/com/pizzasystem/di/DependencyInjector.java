
package com.pizzasystem.di;

import com.pizzasystem.interfaces.IAuthenticator;
import com.pizzasystem.interfaces.IDatabaseManager;
import com.pizzasystem.interfaces.IOrderManager;
import com.pizzasystem.interfaces.IPaymentProcessor;
import com.pizzasystem.services.Authenticator;
import com.pizzasystem.services.DatabaseManager;
import com.pizzasystem.services.OrderManager;
import com.pizzasystem.services.PaymentProcessor;

/**
 * Clase que actúa como contenedor de dependencias centralizado.
 * Esta clase implementa el patrón Singleton para asegurar
 * una única instancia del inyector en toda la aplicación.
 */
public class DependencyInjector {
    private static DependencyInjector instance;

    // Dependencias
    private final IDatabaseManager databaseManager;
    private final IAuthenticator authenticator;
    private final IOrderManager orderManager;
    private final IPaymentProcessor paymentProcessor;

    private DependencyInjector() {
        // Inicializar dependencias
        this.databaseManager = new DatabaseManager("jdbc:h2:mem:pizzadb", "sa", "");
        this.databaseManager.connect();

        // Inyectar dependencias en constructores
        this.authenticator = new Authenticator(databaseManager);
        this.orderManager = new OrderManager(databaseManager);
        this.paymentProcessor = new PaymentProcessor(databaseManager);
    }

    /**
     * Obtiene la instancia única del inyector de dependencias.
     * @return La instancia del inyector
     */
    public static synchronized DependencyInjector getInstance() {
        if (instance == null) {
            instance = new DependencyInjector();
        }
        return instance;
    }

    /**
     * Proporciona acceso al gestor de base de datos.
     * @return Instancia de IDatabaseManager
     */
    public IDatabaseManager getDatabaseManager() {
        return databaseManager;
    }

    /**
     * Proporciona acceso al autenticador.
     * @return Instancia de IAuthenticator
     */
    public IAuthenticator getAuthenticator() {
        return authenticator;
    }

    /**
     * Proporciona acceso al gestor de pedidos.
     * @return Instancia de IOrderManager
     */
    public IOrderManager getOrderManager() {
        return orderManager;
    }

    /**
     * Proporciona acceso al procesador de pagos.
     * @return Instancia de IPaymentProcessor
     */
    public IPaymentProcessor getPaymentProcessor() {
        return paymentProcessor;
    }

    /**
     * Libera recursos cuando la aplicación se cierra.
     */
    public void shutdown() {
        if (databaseManager != null) {
            databaseManager.disconnect();
        }
    }
}