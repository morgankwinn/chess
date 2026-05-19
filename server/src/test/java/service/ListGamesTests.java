package service;

import chess.ChessGame;
import dataaccess.*;
import model.Game;
import request.ListGamesRequest;
import request.RegisterRequest;
import result.ListGamesResult;
import result.RegisterResult;

public class ListGamesTests {
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

            gameDao.addGame(new Game(1, "user2", "user3", "rematch", new ChessGame()));
            gameDao.addGame(new Game(15, "user4", "user5", "speedChess", new ChessGame()));

            ListGamesRequest listGamesRequest = new ListGamesRequest(registerResult.authToken());
            ListGamesService listGamesService = new ListGamesService();
            ListGamesResult listGamesResult = listGamesService.listGames(listGamesRequest, authDao, gameDao);

            if (gameDao.getListGames() == null) {
                System.out.println("Test 1 failed");
            } else {
                System.out.println(gameDao.getListGames());
                System.out.println("Test 1 passed!");
            }
        } catch (Exception e) {
            System.out.println("ERROR: Test 1 failed, " + e);
        }

        System.out.println();
        System.out.println("Test 2: Negative");

        try {
            gameDao.clearGames();
            System.out.println(gameDao.getListGames());

            if (!gameDao.getListGames().isEmpty()) {
                System.out.println("ERROR: Test 2 failed, games list does not clear");
            } else {
                throw new Exception();
            }
        } catch (Exception e) {
            System.out.println("Test 2 passed!");
        }
    }
}
