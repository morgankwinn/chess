package service;

import chess.ChessGame;
import dataaccess.*;
import request.CreateGameRequest;
import request.JoinGameRequest;
import request.RegisterRequest;
import result.CreateGameResult;
import result.RegisterResult;

public class JoinGameTests {
    public static void main(String[] args) {
        final UserDAO userDao = new MemoryUserDAO();
        final GameDAO gameDao = new MemoryGameDAO();
        final AuthDAO authDao = new MemoryAuthDAO();
        int gameID = 0;

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
            gameID = createGameResult.gameID();

            JoinGameRequest joinGameRequest = new JoinGameRequest(registerResult.authToken(), ChessGame.TeamColor.WHITE, gameID);
            JoinGameService joinGameService = new JoinGameService();
            joinGameService.joinGame(joinGameRequest, gameDao, authDao);

            System.out.println("Test 1 passed!");
        } catch (Exception e) {
            System.out.println("ERROR: Test 1 failed, " + e);
        }

        System.out.println();
        System.out.println("Test 2: Negative");

        RegisterRequest registerRequest2 = new RegisterRequest("user2", "1234", "2@gmail.com");
        RegisterService registerService2 = new RegisterService();

        try {
            RegisterResult registerResult2 = registerService2.register(registerRequest2, userDao, authDao);
            System.out.println(gameDao.getListGames());

            JoinGameRequest joinGameRequest2 = new JoinGameRequest(registerResult2.authToken(), ChessGame.TeamColor.WHITE, gameID);
            JoinGameService joinGameService2 = new JoinGameService();
            joinGameService2.joinGame(joinGameRequest2, gameDao, authDao);

            System.out.println("ERROR: Test 2 failed, Team Color is taken");
        } catch (Exception e) {
            System.out.println("Test 2 passed!");
        }
    }
}
