package service;

import dataaccess.AuthDAO;
import dataaccess.DataAccessException;
import dataaccess.UserDAO;
import model.AuthToken;
import model.User;
import org.mindrot.jbcrypt.BCrypt;
import model.request.RegisterRequest;
import model.result.RegisterResult;

public class RegisterService {

    public RegisterResult register(RegisterRequest request, UserDAO userDao, AuthDAO authDao)
            throws AlreadyTakenException, BadRequestException, DataAccessException {
        String username = request.username();
        String password = request.password();
        String email = request.email();

        validateRequest(username, password, email);
        isUserOpen(username, userDao);

        password = BCrypt.hashpw(password, BCrypt.gensalt());
        User user = new User(username, password, email);
        userDao.addUser(user);
        AuthToken authToken = authDao.addAuthToken(user);

        return new RegisterResult(username, authToken.authToken());
    }

    private void validateRequest(String username, String password, String email) throws BadRequestException {
        if (username == null || password == null || email == null) {
            throw new BadRequestException("ERROR: required field empty");
        }
    }

    private void isUserOpen(String username, UserDAO userDao) throws AlreadyTakenException, DataAccessException {
        if (userDao.getUser(username) != null) {
            throw new AlreadyTakenException("ERROR: username already taken");
        }
    }
}
