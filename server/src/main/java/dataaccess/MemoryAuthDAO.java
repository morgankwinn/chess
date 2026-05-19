package dataaccess;

import model.AuthToken;
import model.User;

import java.util.HashMap;

public class MemoryAuthDAO implements AuthDAO {
    private final HashMap<AuthToken, User> authTokens = new HashMap<>();

    @Override
    public AuthToken addAuthToken(User user) {
        AuthToken authToken = createAuthToken();
        authTokens.put(authToken, user);
        return authToken;
    }

    @Override
    public void clearAuthTokens() {
        authTokens.clear();
    }

    @Override
    public boolean containsAuthToken(String authToken) {
        return authTokens.containsKey(new AuthToken(authToken));
    }

    @Override
    public User getUser(String authToken) {
        return authTokens.get(new AuthToken(authToken));
    }

    @Override
    public void deleteAuthToken(String authToken) {
        authTokens.remove(new AuthToken(authToken));
    }
}
