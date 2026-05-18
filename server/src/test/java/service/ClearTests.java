package service;

import dataaccess.*;
import request.RegisterRequest;
import result.RegisterResult;

public class ClearTests {
    public static void main(String[] args) {
        final UserDAO userDao = new MemoryUserDAO();
        final AuthDAO authDao = new MemoryAuthDAO();
        final GameDAO gameDao = new MemoryGameDAO();

        System.out.println("Test 1");
        RegisterRequest request = new RegisterRequest("user1", "1234", "me@gmail.com");
        RegisterService registerService = new RegisterService();
        ClearService clearService = new ClearService();

        try {
            RegisterResult result = registerService.register(request, userDao, authDao);

            System.out.println("Username: " + result.username());
            System.out.println("Auth Token: " + result.authToken());

            clearService.clear(userDao, authDao, gameDao);
            System.out.println();
            System.out.println("Cleared");
            System.out.println();

            System.out.println("User: " + userDao.getUser("user1"));

            if (userDao.getUser("user1") == null) {
                System.out.println("Test 1 passed!");
            } else {
                System.out.println("ERROR: Test 1 failed, did not clear correctly");
            }
        } catch (Exception e) {
            System.out.println("ERROR: Test 1 failed, exception received");
        }
    }
}
