package service;

import dataaccess.AuthDAO;
import dataaccess.MemoryAuthDAO;
import dataaccess.MemoryUserDAO;
import dataaccess.UserDAO;
import request.LoginRequest;
import request.RegisterRequest;
import result.LoginResult;
import result.RegisterResult;

public class LoginTests {
    public static void main(String[] args) {
        final UserDAO userDao = new MemoryUserDAO();
        final AuthDAO authDao = new MemoryAuthDAO();

        System.out.println("Test 1: Positive");
        RegisterRequest registerRequest = new RegisterRequest("user1", "1234", "me@gmail.com");
        RegisterService registerService = new RegisterService();

        try {
            RegisterResult registerResult = registerService.register(registerRequest, userDao, authDao);

            System.out.println("Username: " + registerResult.username());
            System.out.println("Auth Token: " + registerResult.authToken());

            LoginRequest loginRequest = new LoginRequest("user1", "1234");
            LoginService loginService = new LoginService();
            LoginResult loginResult = loginService.login(loginRequest, userDao, authDao);

            System.out.println("Username: " + loginResult.username());
            System.out.println("Auth Token: " + loginResult.authToken());

            System.out.println("Test 1 passed!");
        } catch (Exception e) {
            System.out.println("ERROR: Test 1 failed, " + e);
        }

        System.out.println();
        System.out.println("Test 2: Negative");

        try {
            LoginRequest loginRequest = new LoginRequest("user1", "5678");
            LoginService loginService = new LoginService();
            LoginResult loginResult = loginService.login(loginRequest, userDao, authDao);

            System.out.println("Username: " + loginResult.username());
            System.out.println("Auth Token: " + loginResult.authToken());

            System.out.println("ERROR: Test 2 failed, used incorrect password");
        } catch (Exception e) {
            System.out.println("Test 2 passed!");
        }
    }
}
