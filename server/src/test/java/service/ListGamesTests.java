package service;

import dataaccess.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import request.ListGamesRequest;
import request.RegisterRequest;
import result.ListGamesResult;
import result.RegisterResult;

public class ListGamesTests {
    private static UserDAO userDao;
    private static GameDAO gameDao;
    private static AuthDAO authDao;
    private static RegisterResult registerResult;

    @BeforeEach
    public void setup() throws BadRequestException, AlreadyTakenException {
        userDao = new MemoryUserDAO();
        gameDao = new MemoryGameDAO();
        authDao = new MemoryAuthDAO();

        RegisterRequest registerRequest = new RegisterRequest("user1", "1234", "1@gmail.com");
        RegisterService registerService = new RegisterService();
        registerResult = registerService.register(registerRequest, userDao, authDao);

        gameDao.addGame("user2", "user3", "rematch");
        gameDao.addGame("user4", "user5", "speedChess");
    }

    @Test
    public void listGamesSuccess() throws UnauthorizedException, BadRequestException {
        ListGamesRequest listGamesRequest = new ListGamesRequest(registerResult.authToken());
        ListGamesService listGamesService = new ListGamesService();
        ListGamesResult listGamesResult = listGamesService.listGames(listGamesRequest, authDao, gameDao);

        Assertions.assertNotNull(listGamesResult.games());
    }

    @Test
    public void listGamesUnauthorized() {
        ListGamesRequest listGamesRequest = new ListGamesRequest("");
        ListGamesService listGamesService = new ListGamesService();

        try {
            listGamesService.listGames(listGamesRequest, authDao, gameDao);
        } catch (Exception e) {
            Assertions.assertEquals(UnauthorizedException.class, e.getClass());
        }
    }
}
