package service;

import dataaccess.AuthDAO;
import dataaccess.DataAccessException;
import dataaccess.GameDAO;
import model.request.CreateGameRequest;
import model.result.CreateGameResult;

public class CreateGameService {
    public CreateGameResult createGame(CreateGameRequest request, GameDAO gameDao, AuthDAO authDao)
            throws UnauthorizedException, BadRequestException, DataAccessException {
        try {
            String authToken = request.authToken();

            if (authToken == null) {
                throw new BadRequestException("ERROR: no Auth Token");
            } else if (request.gameName() == null) {
                throw new BadRequestException("ERROR: no Game Name");
            } else if (!authDao.containsAuthToken(authToken)) {
                throw new UnauthorizedException("ERROR: Auth Token invalid");
            }

            int gameID = gameDao.addGame(null, null, request.gameName());
            return new CreateGameResult(gameID);
        } catch (NullPointerException e) {
            throw new UnauthorizedException("ERROR: no Auth Token");
        }
    }
}
