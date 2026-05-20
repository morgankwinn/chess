package service;

import dataaccess.AuthDAO;
import dataaccess.MemoryAuthDAO;
import dataaccess.MemoryUserDAO;
import dataaccess.UserDAO;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import request.RegisterRequest;
import result.RegisterResult;

public class RegisterTests {
    private static UserDAO userDao;
    private static AuthDAO authDao;

    @BeforeEach
    public void setup() {
        userDao = new MemoryUserDAO();
        authDao = new MemoryAuthDAO();
    }

    @Test
    public void registerSuccess() throws BadRequestException, AlreadyTakenException {
        RegisterRequest request = new RegisterRequest("user1", "1234", "me@gmail.com");
        RegisterService service = new RegisterService();
        RegisterResult result = service.register(request, userDao, authDao);

        Assertions.assertNotNull(result.authToken());
        Assertions.assertNotEquals("", result.authToken());
    }

    @Test
    public void reregisterFailure() throws BadRequestException, AlreadyTakenException {
        RegisterRequest request = new RegisterRequest("user1", "1234", "me@gmail.com");
        RegisterService service = new RegisterService();
        service.register(request, userDao, authDao);

        try {
            service.register(request, userDao, authDao);
        } catch (Exception e) {
            Assertions.assertEquals(AlreadyTakenException.class, e.getClass());
        }
    }
}
