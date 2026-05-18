package server;

import com.google.gson.Gson;
import dataaccess.AuthDAO;
import dataaccess.MemoryAuthDAO;
import dataaccess.MemoryUserDAO;
import dataaccess.UserDAO;
import handler.RegisterHandler;
import io.javalin.*;
import io.javalin.http.Context;
import service.AlreadyTakenException;

public class Server {

    private final Javalin javalin;
    private final UserDAO userDao = new MemoryUserDAO();
    private final AuthDAO authDao = new MemoryAuthDAO();

    public Server() {
        javalin = Javalin.create(config -> config.staticFiles.add("web"))
                .post("/user", context -> RegisterHandler.handleRegister(context, userDao, authDao))
                .exception(AlreadyTakenException.class, this::handleException);

        // Register your endpoints and exception handlers here.
    }

    public int run(int desiredPort) {
        javalin.start(desiredPort);
        return javalin.port();
    }

    public void stop() {
        javalin.stop();
    }

    private String handleException(AlreadyTakenException e, Context context) {
        return new Gson().toJson(new Exception(e + ": " + context.body()));
    }
}
