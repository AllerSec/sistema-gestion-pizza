package com.pizzasystem.interfaces;

import com.pizzasystem.models.User;
import java.util.Optional;

public interface IAuthenticator {
    boolean login(String username, String password);
    boolean logout(String username);
    boolean register(User user);
    Optional<User> getCurrentUser();
}