package client;

import chess.ChessGame;
import model.Game;
import model.result.*;
import org.junit.jupiter.api.*;
import server.Server;
import server.ServerFacade;

import java.util.ArrayList;

public class ServerFacadeTests {
    private static Server server;
    private static ServerFacade serverFacade;

    @BeforeAll
    public static void init() {
        server = new Server();
        var port = server.run(0);
        System.out.println("Started test HTTP server on " + port);

        serverFacade = new ServerFacade("http://localhost:" + port);
    }

    @BeforeEach
    public void setup() {
        serverFacade.clear();
    }

    @AfterAll
    public static void stopServer() {
        server.stop();
    }

    @Test
    public void clearSuccess() throws RuntimeException {
        RegisterResult registerResult = serverFacade.register(
                "user1", "password", "1@mail.com"
        );
        CreateGameResult createGameResult = serverFacade.createGame(registerResult.authToken(), "game1");
        Assertions.assertNotNull(registerResult.authToken());
        Assertions.assertNotEquals("", registerResult.authToken());
        Assertions.assertNotEquals(0, createGameResult.gameID());

        serverFacade.clear();
    }

    @Test
    public void createGameSuccess() throws RuntimeException {
        RegisterResult registerResult = serverFacade.register(
                "user1", "password", "1@mail.com"
        );
        CreateGameResult createGameResult = serverFacade.createGame(registerResult.authToken(), "game1");
        Assertions.assertNotNull(registerResult.authToken());
        Assertions.assertNotEquals("", registerResult.authToken());
        Assertions.assertNotEquals(0, createGameResult.gameID());
    }

    @Test
    public void createGameNoAuthToken() {
        try {
            serverFacade.createGame("", "game1");
        } catch (Exception e) {
            Assertions.assertEquals(RuntimeException.class, e.getClass());
        }
    }

    @Test
    public void joinGameSuccess() throws RuntimeException {
        RegisterResult registerResult = serverFacade.register(
                "user1", "password", "1@mail.com"
        );
        CreateGameResult createGameResult = serverFacade.createGame(registerResult.authToken(), "game1");
        Assertions.assertNotNull(registerResult.authToken());
        Assertions.assertNotEquals("", registerResult.authToken());
        Assertions.assertNotEquals(0, createGameResult.gameID());

        serverFacade.joinGame(registerResult.authToken(), ChessGame.TeamColor.WHITE, createGameResult.gameID());
    }

    @Test
    public void joinGameDuplicateSide() throws RuntimeException {
        RegisterResult registerResult = serverFacade.register(
                "user1", "password", "1@mail.com"
        );
        CreateGameResult createGameResult = serverFacade.createGame(registerResult.authToken(), "game1");
        Assertions.assertNotNull(registerResult.authToken());
        Assertions.assertNotEquals("", registerResult.authToken());
        Assertions.assertNotEquals(0, createGameResult.gameID());

        serverFacade.joinGame(registerResult.authToken(), ChessGame.TeamColor.WHITE, createGameResult.gameID());

        try {
            serverFacade.joinGame(registerResult.authToken(), ChessGame.TeamColor.WHITE, createGameResult.gameID());
        } catch (Exception e) {
            Assertions.assertEquals(RuntimeException.class, e.getClass());
        }
    }

    @Test
    public void listGamesSuccess() throws RuntimeException {
        RegisterResult registerResult = serverFacade.register(
                "user1", "password", "1@mail.com"
        );
        CreateGameResult createGameResult = serverFacade.createGame(registerResult.authToken(), "game1");
        Assertions.assertNotNull(registerResult.authToken());
        Assertions.assertNotEquals("", registerResult.authToken());
        Assertions.assertNotEquals(0, createGameResult.gameID());

        ListGamesResult listGamesResult = serverFacade.listGames(registerResult.authToken());
        Assertions.assertNotNull(listGamesResult.games());
        Assertions.assertNotEquals(new ArrayList<Game>(), listGamesResult.games());
    }

    @Test
    public void listGamesNoAuthToken() {
        try {
            serverFacade.listGames("");
        } catch (Exception e) {
            Assertions.assertEquals(RuntimeException.class, e.getClass());
        }
    }

    @Test
    public void loginSuccess() throws RuntimeException {
        RegisterResult registerResult = serverFacade.register(
                "user1", "password", "1@mail.com"
        );
        Assertions.assertNotNull(registerResult.authToken());
        Assertions.assertNotEquals("", registerResult.authToken());

        LoginResult loginResult = serverFacade.login("user1", "password");
        Assertions.assertNotNull(loginResult.authToken());
    }

    @Test
    public void loginUserDoesNotExist() {
        try {
            serverFacade.login("user1", "password");
        } catch (Exception e) {
            Assertions.assertEquals(RuntimeException.class, e.getClass());
        }
    }

    @Test
    public void logoutSuccess() throws RuntimeException {
        RegisterResult registerResult = serverFacade.register(
                "user1", "password", "1@mail.com"
        );
        Assertions.assertNotNull(registerResult.authToken());
        Assertions.assertNotEquals("", registerResult.authToken());

        serverFacade.logout(registerResult.authToken());
    }

    @Test
    public void logoutFailure() {
        try {
            serverFacade.logout("");
        } catch (Exception e) {
            Assertions.assertEquals(RuntimeException.class, e.getClass());
        }
    }

    @Test
    public void registerSuccess() throws RuntimeException {
        RegisterResult registerResult = serverFacade.register(
                "user1", "password", "1@mail.com"
        );
        Assertions.assertNotNull(registerResult.authToken());
        Assertions.assertNotEquals("", registerResult.authToken());
    }

    @Test
    public void registerUsernameNull() {
        try {
            serverFacade.register(
                    null, "password", "1@mail.com"
            );
        } catch (Exception e) {
            Assertions.assertEquals(RuntimeException.class, e.getClass());
        }
    }
}
