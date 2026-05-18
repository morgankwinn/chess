package dataaccess;

import model.AuthToken;
import model.User;

public interface AuthDAO {
    AuthToken addAuthToken(User user);

    void clearAuthTokens();
}
