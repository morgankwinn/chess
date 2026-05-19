package dataaccess;

import model.AuthToken;
import model.User;

import java.util.UUID;

public interface AuthDAO {
    AuthToken addAuthToken(User user);

    void clearAuthTokens();

    boolean containsAuthToken(String authToken);

    User getUser(String authToken);

    void deleteAuthToken(String authToken);

    default AuthToken createAuthToken() {
        return new AuthToken(UUID.randomUUID().toString());
    }
}
