package com.pizzasystem.services;


import com.pizzasystem.interfaces.IAuthenticator;
import com.pizzasystem.interfaces.IDatabaseManager;
import com.pizzasystem.models.User;

import java.util.List;
import java.util.Optional;

public class Authenticator implements IAuthenticator {
    private final IDatabaseManager databaseManager;
    private User currentUser;

    public Authenticator(IDatabaseManager databaseManager) {
        this.databaseManager = databaseManager;
    }

    @Override
    public boolean login(String username, String password) {
        List<User> users = databaseManager.findAll(User.class);

        for (User user : users) {
            if (user.getUsername().equals(username) && user.getPassword().equals(password)) {
                this.currentUser = user;
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean logout(String username) {
        if (currentUser != null && currentUser.getUsername().equals(username)) {
            currentUser = null;
            return true;
        }
        return false;
    }

    @Override
    public boolean register(User user) {
        return databaseManager.save(user);
    }

    @Override
    public Optional<User> getCurrentUser() {
        return Optional.ofNullable(currentUser);
    }
}
