package service;

import dataaccess.AuthDAO;
import dataaccess.GameDAO;
import dataaccess.UserDAO;

public class ClearService {
    public void clear(UserDAO userDao, AuthDAO authDao, GameDAO gameDao) {
        clearUsers(userDao);
        clearAuthTokens(authDao);
        clearGames(gameDao);
    }

    private void clearUsers(UserDAO userDao) {
        userDao.clearUsers();
    }

    private void clearAuthTokens(AuthDAO authDao) {
        authDao.clearAuthTokens();
    }

    private void clearGames(GameDAO gameDao) {
        gameDao.clearGames();
    }
}
