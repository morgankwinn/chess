package dataaccess;

import chess.ChessGame;
import model.AuthToken;
import model.Game;
import model.User;
import org.junit.jupiter.api.*;
import model.request.RegisterRequest;
import model.result.RegisterResult;
import service.AlreadyTakenException;
import service.BadRequestException;
import service.RegisterService;

import java.util.Collection;

public class DAOTests {
    private static AuthDAO authDAO = new MySQLAuthDAO();
    private static GameDAO gameDAO = new MySQLGameDAO();
    private static UserDAO userDAO = new MySQLUserDAO();
    private static User testUser = new User("testUser", "userNumber1", "User1@user.net");

    @AfterEach
    public void takeDown() throws DataAccessException {
        authDAO.clearAuthTokens();
        gameDAO.clearGames();
        userDAO.clearUsers();
    }

    @Test
    public void addAuthTokenSuccess() throws Exception {
        AuthToken authToken = authDAO.addAuthToken(testUser);
        Assertions.assertNotNull(authToken);
    }

    @Test
    public void addAuthTokenNoPassword() {
        try {
            authDAO.addAuthToken(new User("badUser", null, "email@mail.com"));
        } catch (Exception e) {
            Assertions.assertEquals(DataAccessException.class, e.getClass());
        }

    }

    @Test
    public void clearAuthTokensSuccess() throws BadRequestException, AlreadyTakenException, DataAccessException {
        RegisterRequest registerRequest =
                new RegisterRequest(testUser.username(), testUser.password(), testUser.email());
        RegisterService registerService = new RegisterService();
        RegisterResult registerResult = registerService.register(registerRequest, userDAO, authDAO);

        Assertions.assertNotNull(authDAO.getUser(registerResult.authToken()));

        authDAO.clearAuthTokens();

        Assertions.assertNull(authDAO.getUser(registerResult.authToken()));
    }

    @Test
    public void containsAuthTokenSuccess() throws BadRequestException, AlreadyTakenException, DataAccessException {
        RegisterRequest registerRequest =
                new RegisterRequest(testUser.username(), testUser.password(), testUser.email());
        RegisterService registerService = new RegisterService();
        RegisterResult registerResult = registerService.register(registerRequest, userDAO, authDAO);

        boolean contains = authDAO.containsAuthToken(registerResult.authToken());
        Assertions.assertTrue(contains);
    }

    @Test
    public void containsAuthTokenFailure() throws DataAccessException {
        boolean contains = authDAO.containsAuthToken("");
        Assertions.assertFalse(contains);
    }

    @Test
    public void authGetUserSuccess() throws BadRequestException, AlreadyTakenException, DataAccessException {
        RegisterRequest registerRequest =
                new RegisterRequest(testUser.username(), testUser.password(), testUser.email());
        RegisterService registerService = new RegisterService();
        RegisterResult registerResult = registerService.register(registerRequest, userDAO, authDAO);

        Assertions.assertNotNull(authDAO.getUser(registerResult.authToken()));
    }

    @Test
    public void authGetUserFailure() throws DataAccessException {
        Assertions.assertNull(authDAO.getUser(""));
    }

    @Test
    public void deleteAuthTokenSuccess() throws BadRequestException, AlreadyTakenException, DataAccessException {
        RegisterRequest registerRequest =
                new RegisterRequest(testUser.username(), testUser.password(), testUser.email());
        RegisterService registerService = new RegisterService();
        RegisterResult registerResult = registerService.register(registerRequest, userDAO, authDAO);

        Assertions.assertNotNull(authDAO.getUser(registerResult.authToken()));

        authDAO.deleteAuthToken(registerResult.authToken());

        Assertions.assertFalse(authDAO.containsAuthToken(registerResult.authToken()));
    }

    @Test
    public void deleteAuthTokenFailure() throws BadRequestException, AlreadyTakenException, DataAccessException {
        RegisterRequest registerRequest =
                new RegisterRequest(testUser.username(), testUser.password(), testUser.email());
        RegisterService registerService = new RegisterService();
        RegisterResult registerResult = registerService.register(registerRequest, userDAO, authDAO);

        Assertions.assertNotNull(authDAO.getUser(registerResult.authToken()));

        authDAO.deleteAuthToken("");

        Assertions.assertTrue(authDAO.containsAuthToken(registerResult.authToken()));
    }

    @Test
    public void createAuthTokenSuccess() {
        AuthToken authToken = authDAO.createAuthToken();
        Assertions.assertNotNull(authToken);
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
        gameDAO.clearGames();
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

    @Test
    public void userGetUserSuccess() throws BadRequestException, AlreadyTakenException, DataAccessException {
        RegisterRequest registerRequest =
                new RegisterRequest("user1", "password", "email@mail.com");
        RegisterService registerService = new RegisterService();
        RegisterResult registerResult = registerService.register(registerRequest, userDAO, authDAO);

        User user = userDAO.getUser("user1");
        Assertions.assertNotNull(user);
    }

    @Test
    public void userGetUserFailure() {
        try {
            userDAO.getUser("user1");
        } catch (Exception e) {
            Assertions.assertEquals(DataAccessException.class, e.getClass());
        }
    }

    @Test
    public void addUserSuccess() throws DataAccessException {
        userDAO.addUser(new User("user1", "password", "email@mail.com"));
        Assertions.assertNotNull(userDAO.getUser("user1"));
    }

    @Test
    public void addUserIdenticalUsers() {
        try {
            userDAO.addUser(new User("user1", "password", "email@mail.com"));
            userDAO.addUser(new User("user1", "password", "email@mail.com"));
        } catch (Exception e) {
            Assertions.assertEquals(DataAccessException.class, e.getClass());
        }
    }

    @Test
    public void clearUsersSuccess() throws DataAccessException {
        userDAO.addUser(new User("user1", "password", "email@mail.com"));
        userDAO.addUser(new User("user2", "password", "email@mail.com"));
        userDAO.addUser(new User("user3", "password", "email@mail.com"));

        userDAO.clearUsers();

        Assertions.assertNull(userDAO.getUser("user1"));
        Assertions.assertNull(userDAO.getUser("user2"));
        Assertions.assertNull(userDAO.getUser("user3"));
    }
}
