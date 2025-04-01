package com.pizzasystem.services;

import com.pizzasystem.models.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

public class AuthenticatorTest {

    private DatabaseManager db;
    private Authenticator authenticator;

    @BeforeEach
    public void setup() {
        db = new DatabaseManager("jdbc:h2:mem:testdb", "sa", "");
        db.connect();
        authenticator = new Authenticator(db);
    }

    @AfterEach
    public void tearDown() {
        db.disconnect();
    }

    @Test
    public void testRegisterAndLoginLogout() {
        User user = new User(1L, "testuser", "testpass", "Test User", "test@example.com", "Test Address", "123456");
        boolean registered = authenticator.register(user);
        assertTrue(registered, "El usuario debe registrarse correctamente");

        boolean loginSuccess = authenticator.login("testuser", "testpass");
        assertTrue(loginSuccess, "El usuario debe poder iniciar sesión con credenciales correctas");

        Optional<User> currentUser = authenticator.getCurrentUser();
        assertTrue(currentUser.isPresent(), "Debe haber un usuario activo tras el login");
        assertEquals("testuser", currentUser.get().getUsername(), "El nombre de usuario debe coincidir");

        boolean logoutSuccess = authenticator.logout("testuser");
        assertTrue(logoutSuccess, "El usuario debe poder cerrar sesión");
        assertFalse(authenticator.getCurrentUser().isPresent(), "No debe haber usuario activo tras el logout");
    }
}
