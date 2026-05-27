package service;

import dataaccess.AuthDAO;
import dataaccess.DataAccessException;
import dataaccess.GameDAO;
import dataaccess.UserDAO;

public class ClearService {
    public void clear(UserDAO userDao, AuthDAO authDao, GameDAO gameDao) throws DataAccessException {
        clearUsers(userDao);
        clearAuthTokens(authDao);
        clearGames(gameDao);
    }

    private void clearUsers(UserDAO userDao) {
        userDao.clearUsers();
    }

    private void clearAuthTokens(AuthDAO authDao) throws DataAccessException {
        authDao.clearAuthTokens();
    }

    private void clearGames(GameDAO gameDao) throws DataAccessException {
        gameDao.clearGames();
    }
}
