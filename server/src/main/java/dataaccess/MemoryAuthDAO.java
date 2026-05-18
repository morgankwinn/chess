package dataaccess;

import model.AuthToken;
import model.User;

import java.util.HashMap;
import java.util.UUID;

public class MemoryAuthDAO implements AuthDAO {
    private final HashMap<User, AuthToken> authTokens = new HashMap<>();

    @Override
    public AuthToken addAuth(User user) {
        AuthToken authToken = createAuth();
        authTokens.put(user, authToken);
        return authToken;
    }

    private AuthToken createAuth() {
        return new AuthToken(UUID.randomUUID().toString());
    }
}
