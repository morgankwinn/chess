package dataaccess;

import chess.ChessGame;
import model.Game;
import org.junit.jupiter.api.*;
import passoff.server.TestServerFacade;
import server.Server;

import java.util.Collection;

public class MySQLGameDAOTests {
    private static GameDAO gameDAO = new MySQLGameDAO();
    private static TestServerFacade serverFacade;
    private static Server server;

    @BeforeAll
    public static void startServer() {
        server = new Server();
        var port = server.run(0);
        System.out.println("Started test HTTP server on " + port);

        serverFacade = new TestServerFacade("localhost", Integer.toString(port));
    }

    @BeforeEach
    public void setup() {
        serverFacade.clear();
    }

    @AfterAll
    static void stopServer() {
        server.stop();
    }

    @Test
    public void clearGamesSuccess() throws DataAccessException {
        int id = gameDAO.addGame("haha", "hehe", "wahoo");
        Assertions.assertNotEquals(0, id);

        gameDAO.clearGames();
        Assertions.assertNull(gameDAO.getGame(1));
    }

    @Test
    public void addGameSuccess() throws DataAccessException {
        gameDAO.addGame("1", "2", "newGame");
        Assertions.assertNotNull(gameDAO.getGame(1));
    }

    @Test
    public void addGameFailure() {
        try {
            gameDAO.addGame("1", "2", null);
        } catch (Exception e) {
            Assertions.assertEquals(DataAccessException.class, e.getClass());
        }
    }

    @Test
    public void getGameSuccess() throws DataAccessException {
        int id = gameDAO.addGame("hehe", "haha", "wahoo");
        Assertions.assertNotNull(gameDAO.getGame(id));
    }

    @Test
    public void getGameFailure() {
        try {
            gameDAO.getGame(0);
        } catch (Exception e) {
            Assertions.assertEquals(DataAccessException.class, e.getClass());
        }
    }

    @Test
    public void getListGamesSuccess() throws DataAccessException {
        gameDAO.addGame("hehe", "haha", "wahoo");
        Collection<Game> games = gameDAO.getListGames();
        Assertions.assertTrue(
                games.contains(
                        new Game(
                                1,
                                "hehe",
                                "haha",
                                "wahoo",
                                new ChessGame()
                        )
                )
        );
    }

    @Test
    public void getListGamesFailure() {
        try {
            gameDAO.getListGames();
        } catch (Exception e) {
            Assertions.assertEquals(DataAccessException.class, e.getClass());
        }
    }

    @Test
    public void addUserToGameSuccess() throws DataAccessException {
        gameDAO.addGame(null, null, "yeehee");
        Assertions.assertNull(gameDAO.getGame(1).whiteUsername());
        Assertions.assertNull(gameDAO.getGame(1).blackUsername());

        gameDAO.addUserToGame(1, "player1", ChessGame.TeamColor.BLACK);
        Assertions.assertEquals("player1", gameDAO.getGame(1).blackUsername());
        gameDAO.addUserToGame(1, "player2", ChessGame.TeamColor.WHITE);
        Assertions.assertEquals("player2", gameDAO.getGame(1).whiteUsername());
    }

    @Test
    public void addUserToGameFailure() throws DataAccessException {
        gameDAO.addGame(null, null, "yeehee");
        Assertions.assertNull(gameDAO.getGame(1).whiteUsername());
        Assertions.assertNull(gameDAO.getGame(1).blackUsername());

        gameDAO.addUserToGame(1, "player1", ChessGame.TeamColor.BLACK);
        Assertions.assertEquals("player1", gameDAO.getGame(1).blackUsername());

        try {
            gameDAO.addUserToGame(1, "player2", ChessGame.TeamColor.BLACK);
        } catch (Exception e) {
            Assertions.assertEquals(DataAccessException.class, e.getClass());
        }
    }
}
