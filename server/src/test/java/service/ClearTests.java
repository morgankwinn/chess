package service;

import dataaccess.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import request.RegisterRequest;
import result.RegisterResult;

public class ClearTests {
    private static UserDAO userDao;
    private static AuthDAO authDao;
    private static GameDAO gameDao;

    @BeforeAll
    public static void setup() {
        userDao = new MemoryUserDAO();
        authDao = new MemoryAuthDAO();
        gameDao = new MemoryGameDAO();
    }

    @Test
    public void clearSuccess() throws BadRequestException, AlreadyTakenException {
        RegisterRequest request = new RegisterRequest("user1", "1234", "me@gmail.com");
        RegisterService registerService = new RegisterService();
        ClearService clearService = new ClearService();

        RegisterResult result = registerService.register(request, userDao, authDao);
        clearService.clear(userDao, authDao, gameDao);

        Assertions.assertNull(userDao.getUser("user1"));
    }
}
