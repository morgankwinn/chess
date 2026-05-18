package dataaccess;

import model.User;

public interface UserDAO {
    User getUser(String username);

    void addUser(User user);
}
