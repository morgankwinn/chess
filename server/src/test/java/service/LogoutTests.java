package service;

import dataaccess.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import model.request.LogoutRequest;
import model.request.RegisterRequest;
import model.result.RegisterResult;

public class LogoutTests {
    private static UserDAO userDao;
    private static AuthDAO authDao;
    private static RegisterResult registerResult;

    @BeforeEach
    public void setup() throws BadRequestException, AlreadyTakenException, DataAccessException {
        userDao = new MemoryUserDAO();
        authDao = new MemoryAuthDAO();

        RegisterRequest registerRequest = new RegisterRequest("user1", "1234", "1@gmail.com");
        RegisterService registerService = new RegisterService();
        registerResult = registerService.register(registerRequest, userDao, authDao);
    }

    @Test
    public void logoutSuccess() throws UnauthorizedException, BadRequestException, DataAccessException {
        LogoutRequest logoutRequest = new LogoutRequest(registerResult.authToken());
        LogoutService logoutService = new LogoutService();
        logoutService.logout(logoutRequest, authDao);
    }

    @Test
    public void logoutIncorrectAuthToken() {
        LogoutRequest logoutRequest = new LogoutRequest("");
        LogoutService logoutService = new LogoutService();

        try {
            logoutService.logout(logoutRequest, authDao);
        } catch (Exception e) {
            Assertions.assertEquals(UnauthorizedException.class, e.getClass());
        }
    }
}
