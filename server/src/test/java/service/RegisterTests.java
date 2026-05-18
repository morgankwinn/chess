package service;

import dataaccess.AuthDAO;
import dataaccess.MemoryAuthDAO;
import dataaccess.MemoryUserDAO;
import dataaccess.UserDAO;
import request.RegisterRequest;
import result.RegisterResult;

public class RegisterTests {
    public static void main(String[] args) {
        final UserDAO userDao = new MemoryUserDAO();
        final AuthDAO authDao = new MemoryAuthDAO();

        System.out.println("Test 1: Positive");
        RegisterRequest request = new RegisterRequest("user1", "1234", "me@gmail.com");
        RegisterService service = new RegisterService();

        try {
            RegisterResult result = service.register(request, userDao, authDao);

            System.out.println("Username: " + result.username());
            System.out.println("Auth Token: " + result.authToken().authToken());

            System.out.println("Test 1 passed!");
        } catch (AlreadyTakenException e) {
            System.out.println("ERROR: Test 1 failed");
        }

        System.out.println();
        System.out.println("Test 2: Negative");
        RegisterRequest request2 = new RegisterRequest("user1", "1234", "me@gmail.com");
        RegisterService service2 = new RegisterService();

        try {
            RegisterResult result2 = service2.register(request2, userDao, authDao);

            System.out.println("ERROR: Test 2 failed");
        } catch (AlreadyTakenException e) {
            System.out.println("Test 2 passed!");
        }
    }
}
