package dataaccess;

import model.User;
import org.junit.jupiter.api.*;
import passoff.server.TestServerFacade;
import request.RegisterRequest;
import result.RegisterResult;
import server.Server;
import service.AlreadyTakenException;
import service.BadRequestException;
import service.RegisterService;

public class MySQLUserDAOTests {
    private static AuthDAO authDAO = new MySQLAuthDAO();
    private static UserDAO userDAO = new MySQLUserDAO();
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
    public void getUserSuccess() throws BadRequestException, AlreadyTakenException, DataAccessException {
        RegisterRequest registerRequest =
                new RegisterRequest("user1", "password", "email@mail.com");
        RegisterService registerService = new RegisterService();
        RegisterResult registerResult = registerService.register(registerRequest, userDAO, authDAO);

        User user = userDAO.getUser("user1");
        Assertions.assertNotNull(user);
    }

    @Test
    public void getUserFailure() {
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
