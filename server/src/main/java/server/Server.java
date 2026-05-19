package server;

import com.google.gson.Gson;
import dataaccess.*;
import handler.*;
import io.javalin.*;
import io.javalin.http.Context;
import service.AlreadyTakenException;
import service.BadRequestException;
import service.UserDoesNotExistException;
import service.UnauthorizedException;

import java.util.Map;

public class Server {

    private final Javalin javalin;
    private final UserDAO userDao = new MemoryUserDAO();
    private final AuthDAO authDao = new MemoryAuthDAO();
    private final GameDAO gameDao = new MemoryGameDAO();

    public Server() {
        javalin = Javalin.create(config -> config.staticFiles.add("web"))
                .post("/user", context -> RegisterHandler.handleRegister(context, userDao, authDao))
                .post("/session", context -> LoginHandler.handleLogin(context, userDao, authDao))
                .delete("/session", context -> LogoutHandler.handleLogout(context, authDao))
                .get("/game", context -> ListGamesHandler.handleListGames(context, gameDao, authDao))
                .delete("/db", context -> ClearHandler.handleClear(userDao, authDao, gameDao))
                .exception(Exception.class, this::handleException);
    }

    public int run(int desiredPort) {
        javalin.start(desiredPort);
        return javalin.port();
    }

    public void stop() {
        javalin.stop();
    }

    private void handleException(Exception e, Context context) {
        Map<String, String> map = Map.of("message", e.getMessage());

        if (e.getClass() == AlreadyTakenException.class) {
            context.status(403);
        } else if (e.getClass() == BadRequestException.class) {
            context.status(400);
        } else if (e.getClass() == UserDoesNotExistException.class || e.getClass() == UnauthorizedException.class) {
            context.status(401);
        }
        context.result(new Gson().toJson(map));
    }
}
