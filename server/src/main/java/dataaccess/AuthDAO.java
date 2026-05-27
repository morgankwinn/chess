package dataaccess;

import model.AuthToken;
import model.User;

import java.util.UUID;

public interface AuthDAO {
    AuthToken addAuthToken(User user) throws DataAccessException;

    void clearAuthTokens() throws DataAccessException;

    boolean containsAuthToken(String authToken) throws DataAccessException;

    User getUser(String authToken) throws DataAccessException;

    void deleteAuthToken(String authToken) throws DataAccessException;

    default AuthToken createAuthToken() {
        return new AuthToken(UUID.randomUUID().toString());
    }
}
