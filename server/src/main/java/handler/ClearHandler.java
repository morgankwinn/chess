package handler;

import dataaccess.AuthDAO;
import dataaccess.GameDAO;
import dataaccess.UserDAO;
import io.javalin.http.Context;
import service.ClearService;

public class ClearHandler {
    public static void handleClear(UserDAO userDao, AuthDAO authDao, GameDAO gameDao) {
        ClearService service = new ClearService();
        service.clear(userDao, authDao, gameDao);
    }
}
