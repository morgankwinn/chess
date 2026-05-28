package dataaccess;

import model.AuthToken;
import model.User;
import org.junit.jupiter.api.*;
import passoff.server.TestServerFacade;
import request.RegisterRequest;
import result.RegisterResult;
import server.Server;
import service.AlreadyTakenException;
import service.BadRequestException;
import service.RegisterService;

public class MySQLAuthDAOTests {
    private static AuthDAO authDAO = new MySQLAuthDAO();
    private static UserDAO userDAO = new MySQLUserDAO();
    private static User testUser = new User("testUser", "userNumber1", "User1@user.net");
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
    public void getUserSuccess() throws BadRequestException, AlreadyTakenException, DataAccessException {
        RegisterRequest registerRequest =
                new RegisterRequest(testUser.username(), testUser.password(), testUser.email());
        RegisterService registerService = new RegisterService();
        RegisterResult registerResult = registerService.register(registerRequest, userDAO, authDAO);

        Assertions.assertNotNull(authDAO.getUser(registerResult.authToken()));
    }

    @Test
    public void getUserFailure() throws DataAccessException {
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
}
