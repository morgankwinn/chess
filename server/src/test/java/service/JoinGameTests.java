package service;

import chess.ChessGame;
import dataaccess.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import request.CreateGameRequest;
import request.JoinGameRequest;
import request.RegisterRequest;
import result.CreateGameResult;
import result.RegisterResult;

public class JoinGameTests {
    private static UserDAO userDao;
    private static GameDAO gameDao;
    private static AuthDAO authDao;
    private static int gameID;
    private static RegisterResult registerResult1;
    private static RegisterResult registerResult2;

    @BeforeEach
    public void setup() throws BadRequestException, AlreadyTakenException, UnauthorizedException, DataAccessException {
        userDao = new MemoryUserDAO();
        gameDao = new MemoryGameDAO();
        authDao = new MemoryAuthDAO();
        gameID = 0;

        RegisterRequest registerRequest1 = new RegisterRequest("user1", "1234", "1@gmail.com");
        RegisterRequest registerRequest2 = new RegisterRequest("user2", "5678", "2@gmail.com");
        RegisterService registerService = new RegisterService();
        registerResult1 = registerService.register(registerRequest1, userDao, authDao);
        registerResult2 = registerService.register(registerRequest2, userDao, authDao);

        CreateGameRequest createGameRequest = new CreateGameRequest(registerResult1.authToken(), "newGame");
        CreateGameService createGameService = new CreateGameService();
        CreateGameResult createGameResult = createGameService.createGame(createGameRequest, gameDao, authDao);
        gameID = createGameResult.gameID();
    }

    @Test
    public void joinGameSuccess()
            throws UnauthorizedException, BadRequestException, AlreadyTakenException, DataAccessException {
        JoinGameRequest joinGameRequest = new JoinGameRequest(
                registerResult1.authToken(), ChessGame.TeamColor.WHITE, gameID);
        JoinGameService joinGameService = new JoinGameService();
        joinGameService.joinGame(joinGameRequest, gameDao, authDao);
    }

    @Test
    public void joinGameAlreadyTaken()
            throws UnauthorizedException, AlreadyTakenException, BadRequestException, DataAccessException {
        JoinGameRequest joinGameRequest = new JoinGameRequest(
                registerResult1.authToken(), ChessGame.TeamColor.WHITE, gameID);
        JoinGameRequest joinGameRequest2 = new JoinGameRequest(
                registerResult2.authToken(), ChessGame.TeamColor.WHITE, gameID);
        JoinGameService joinGameService = new JoinGameService();
        joinGameService.joinGame(joinGameRequest, gameDao, authDao);

        try {
            joinGameService.joinGame(joinGameRequest2, gameDao, authDao);
        } catch (Exception e) {
            Assertions.assertEquals(AlreadyTakenException.class, e.getClass());
        }
    }
}
