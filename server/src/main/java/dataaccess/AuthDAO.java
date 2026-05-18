package dataaccess;

import model.AuthToken;
import model.User;

public interface AuthDAO {
    AuthToken addAuth(User user);
}
