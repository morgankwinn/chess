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
    private final UserDAO userDao = new MySQLUserDAO();
    private final AuthDAO authDao = new MySQLAuthDAO();
    private final GameDAO gameDao = new MySQLGameDAO();
    private final WebSocketHandler wsHandler = new WebSocketHandler();

    public Server() {
        try {
            DatabaseManager.configureDatabase();
        } catch (DataAccessException e) {
            System.out.printf(String.valueOf(e));
        }
        javalin = Javalin.create(config -> config.staticFiles.add("web"))
                .post("/user", context -> RegisterHandler.handleRegister(context, userDao, authDao))
                .post("/session", context -> LoginHandler.handleLogin(context, userDao, authDao))
                .delete("/session", context -> LogoutHandler.handleLogout(context, authDao))
                .get("/game", context -> ListGamesHandler.handleListGames(context, gameDao, authDao))
                .post("/game", context -> CreateGameHandler.handleCreateGame(context, gameDao, authDao))
                .put("/game", context -> JoinGameHandler.handleJoinGame(context, gameDao, authDao))
                .delete("/db", context -> ClearHandler.handleClear(userDao, authDao, gameDao))
                .ws("/ws", ws -> {
                    ws.onConnect(wsHandler);
                    ws.onMessage(wsHandler);
                    ws.onClose(wsHandler);
                })
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
        } else if (e.getClass() == UserDoesNotExistException.class) {
            context.status(401);
        } else if (e.getClass() == DataAccessException.class) {
            context.status(400);
        } else if (e.getClass() == UnauthorizedException.class) {
            context.status(401);
        } else if (e.getClass() == RuntimeException.class) {
            context.status(500);
        }
        context.result(new Gson().toJson(map));
    }
}
