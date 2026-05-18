package service;

import dataaccess.AuthDAO;
import dataaccess.UserDAO;
import model.AuthToken;
import model.User;
import request.RegisterRequest;
import result.RegisterResult;

public class RegisterService {

    public RegisterResult register(RegisterRequest request, UserDAO userDao, AuthDAO authDao) throws AlreadyTakenException {
        String username = request.username();
        isUserOpen(username, userDao);

        User user = new User(username, request.password(), request.email());
        userDao.addUser(user);
        AuthToken authToken = authDao.addAuth(user);

        return new RegisterResult(username, authToken);
    }

    private void isUserOpen(String username, UserDAO userDao) throws AlreadyTakenException {
        if (userDao.getUser(username) != null) {
            throw new AlreadyTakenException("ERROR: username already taken");
        }
    }
}
