package com.pizzasystem.di;

import com.pizzasystem.interfaces.IAuthenticator;
import com.pizzasystem.interfaces.IDatabaseManager;
import com.pizzasystem.interfaces.IOrderManager;
import com.pizzasystem.interfaces.IPaymentProcessor;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class DependencyInjectorTest {

    @Test
    public void testSingletonAndDependencies() {
        DependencyInjector injector1 = DependencyInjector.getInstance();
        DependencyInjector injector2 = DependencyInjector.getInstance();
        assertSame(injector1, injector2, "DependencyInjector debe ser un singleton");

        IDatabaseManager dbManager = injector1.getDatabaseManager();
        IAuthenticator authenticator = injector1.getAuthenticator();
        IOrderManager orderManager = injector1.getOrderManager();
        IPaymentProcessor paymentProcessor = injector1.getPaymentProcessor();

        assertNotNull(dbManager, "DatabaseManager no debe ser nulo");
        assertNotNull(authenticator, "Authenticator no debe ser nulo");
        assertNotNull(orderManager, "OrderManager no debe ser nulo");
        assertNotNull(paymentProcessor, "PaymentProcessor no debe ser nulo");
    }
}
