package server;

import dataaccess.AuthDAO;
import dataaccess.MemoryAuthDAO;
import dataaccess.MemoryUserDAO;
import dataaccess.UserDAO;
import handler.RegisterHandler;
import io.javalin.*;

public class Server {

    private final Javalin javalin;
    private final UserDAO userDao = new MemoryUserDAO();
    private final AuthDAO authDao = new MemoryAuthDAO();

    public Server() {
        javalin = Javalin.create(config -> config.staticFiles.add("web"))
                .post("/user", context -> RegisterHandler.handleRegister(context, userDao, authDao));

        // Register your endpoints and exception handlers here.
    }

    public int run(int desiredPort) {
        javalin.start(desiredPort);
        return javalin.port();
    }

    public void stop() {
        javalin.stop();
    }
}
