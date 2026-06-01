package service;

import dataaccess.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import model.request.CreateGameRequest;
import model.request.RegisterRequest;
import model.result.CreateGameResult;
import model.result.RegisterResult;

public class CreateGameTests {
    private static UserDAO userDao;
    private static GameDAO gameDao;
    private static AuthDAO authDao;
    private static RegisterRequest registerRequest;
    private static RegisterService registerService;
    private static RegisterResult registerResult;

    @BeforeAll
    public static void setup() throws BadRequestException, AlreadyTakenException, DataAccessException {
        userDao = new MemoryUserDAO();
        gameDao = new MemoryGameDAO();
        authDao = new MemoryAuthDAO();

        registerRequest = new RegisterRequest("user1", "1234", "me@gmail.com");
        registerService = new RegisterService();
        registerResult = registerService.register(registerRequest, userDao, authDao);
    }

    @Test
    public void createGameSuccess() throws BadRequestException, UnauthorizedException, DataAccessException {
        CreateGameRequest createGameRequest = new CreateGameRequest(registerResult.authToken(), "newGame");
        CreateGameService createGameService = new CreateGameService();
        CreateGameResult createGameResult = createGameService.createGame(createGameRequest, gameDao, authDao);

        Assertions.assertNotNull(createGameResult);
    }

    @Test
    public void createGameNoGameName() throws UnauthorizedException, DataAccessException {
        CreateGameRequest createGameRequest = new CreateGameRequest(registerResult.authToken(), null);
        CreateGameService createGameService = new CreateGameService();
        CreateGameResult createGameResult = null;
        try {
            createGameResult = createGameService.createGame(createGameRequest, gameDao, authDao);
        } catch (BadRequestException e) {
            Assertions.assertNull(createGameResult);
        }
    }
}
