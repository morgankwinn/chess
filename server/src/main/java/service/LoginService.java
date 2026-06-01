package service;

import dataaccess.AuthDAO;
import dataaccess.DataAccessException;
import dataaccess.UserDAO;
import model.AuthToken;
import model.User;
import org.mindrot.jbcrypt.BCrypt;
import model.request.LoginRequest;
import model.result.LoginResult;

public class LoginService {
    public LoginResult login(LoginRequest request, UserDAO userDao, AuthDAO authDao)
            throws UserDoesNotExistException, BadRequestException, UnauthorizedException, DataAccessException {
        String username = request.username();
        String password = request.password();

        validateRequest(username, password);
        doesUserExist(username, userDao);

        User user = userDao.getUser(username);
        if (!BCrypt.checkpw(password, user.password())) {
            throw new UnauthorizedException("ERROR: password is incorrect");
        }
        AuthToken authToken = authDao.addAuthToken(user);

        return new LoginResult(username, authToken.authToken());
    }

    private void validateRequest(String username, String password) throws BadRequestException {
        if (username == null || password == null) {
            throw new BadRequestException("ERROR: required field empty");
        }
    }

    private void doesUserExist(String username, UserDAO userDao)
            throws UserDoesNotExistException, DataAccessException {
        if (userDao.getUser(username) == null) {
            throw new UserDoesNotExistException("ERROR: username does not exist");
        }
    }
}
