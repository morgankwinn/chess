package dataaccess;

import model.User;

import java.util.HashMap;

public class MemoryUserDAO implements UserDAO {
    private final HashMap<String, User> users = new HashMap<>();

    @Override
    public User getUser(String username) {
        return users.get(username);
    }

    @Override
    public void addUser(User user) {
        users.put(user.username(), user);
    }
}
