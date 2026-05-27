package dataaccess;

import model.User;

public interface UserDAO {
    User getUser(String username) throws DataAccessException;

    void addUser(User user) throws DataAccessException;

    void clearUsers() throws DataAccessException;
}
