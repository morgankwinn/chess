package service;

import dataaccess.*;
import request.CreateGameRequest;
import request.ListGamesRequest;
import request.RegisterRequest;
import result.CreateGameResult;
import result.ListGamesResult;
import result.RegisterResult;

public class CreateGameTests {
    public static void main(String[] args) {
        final UserDAO userDao = new MemoryUserDAO();
        final GameDAO gameDao = new MemoryGameDAO();
        final AuthDAO authDao = new MemoryAuthDAO();

        System.out.println("Test 1: Positive");
        RegisterRequest registerRequest = new RegisterRequest("user1", "1234", "me@gmail.com");
        RegisterService registerService = new RegisterService();

        try {
            RegisterResult registerResult = registerService.register(registerRequest, userDao, authDao);

            System.out.println("Username: " + registerResult.username());
            System.out.println("Auth Token: " + registerResult.authToken());

            CreateGameRequest createGameRequest = new CreateGameRequest(registerResult.authToken(), "newGame");
            CreateGameService createGameService = new CreateGameService();
            CreateGameResult createGameResult = createGameService.createGame(createGameRequest, gameDao, authDao);

            System.out.println(createGameResult.gameID());
            System.out.println("Test 1 passed!");
        } catch (Exception e) {
            System.out.println("ERROR: Test 1 failed, " + e);
        }

        System.out.println();
        System.out.println("Test 2: Negative");

        try {
            RegisterResult registerResult = registerService.register(registerRequest, userDao, authDao);
            System.out.println(gameDao.getListGames());

            CreateGameRequest createGameRequest = new CreateGameRequest(registerResult.authToken(), null);
            CreateGameService createGameService = new CreateGameService();
            createGameService.createGame(createGameRequest, gameDao, authDao);

            System.out.println("ERROR: Test 2 failed, Game Name is null");
        } catch (Exception e) {
            System.out.println("Test 2 passed!");
        }
    }
}
