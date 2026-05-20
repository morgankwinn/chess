package service;

import dataaccess.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import request.LoginRequest;
import request.RegisterRequest;
import result.LoginResult;

public class LoginTests {
    private static UserDAO userDao;
    private static AuthDAO authDao;

    @BeforeEach
    public void setup() throws BadRequestException, AlreadyTakenException {
        userDao = new MemoryUserDAO();
        authDao = new MemoryAuthDAO();

        RegisterRequest registerRequest = new RegisterRequest("user1", "1234", "1@gmail.com");
        RegisterService registerService = new RegisterService();
        registerService.register(registerRequest, userDao, authDao);
    }

    @Test
    public void loginSuccess() throws UnauthorizedException, BadRequestException {
        LoginRequest loginRequest = new LoginRequest("user1", "1234");
        LoginService loginService = new LoginService();
        LoginResult loginResult = loginService.login(loginRequest, userDao, authDao);

        Assertions.assertNotNull(loginResult.authToken());
    }

    @Test
    public void loginIncorrectPassword() {
        LoginRequest loginRequest = new LoginRequest("user1", "5678");
        LoginService loginService = new LoginService();

        try {
            LoginResult loginResult = loginService.login(loginRequest, userDao, authDao);
        } catch (Exception e) {
            Assertions.assertEquals(UnauthorizedException.class, e.getClass());
        }

    }
}
