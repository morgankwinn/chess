package dataaccess;

import model.AuthToken;
import model.User;

import java.util.HashMap;
import java.util.UUID;

public class MemoryAuthDAO implements AuthDAO {
    private final HashMap<User, AuthToken> authTokens = new HashMap<>();

    @Override
    public AuthToken addAuthToken(User user) {
        AuthToken authToken = createAuth();
        authTokens.put(user, authToken);
        return authToken;
    }

    @Override
    public void clearAuthTokens() {
        authTokens.clear();
    }

    private AuthToken createAuth() {
        return new AuthToken(UUID.randomUUID().toString());
    }
}
