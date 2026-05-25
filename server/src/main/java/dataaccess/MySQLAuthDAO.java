package dataaccess;

import model.AuthToken;
import model.User;

public class MySQLAuthDAO implements AuthDAO {
    @Override
    public AuthToken addAuthToken(User user) {
        return null;
    }

    @Override
    public void clearAuthTokens() {

    }

    @Override
    public boolean containsAuthToken(String authToken) {
        return false;
    }

    @Override
    public User getUser(String authToken) {
        return null;
    }

    @Override
    public void deleteAuthToken(String authToken) {

    }

    @Override
    public AuthToken createAuthToken() {
        return AuthDAO.super.createAuthToken();
    }
}
