package service;

import dataaccess.AuthDAO;
import dataaccess.GameDAO;
import model.Game;
import request.ListGamesRequest;
import result.ListGamesResult;

import java.util.Collection;

public class ListGamesService {
    public ListGamesResult listGames(ListGamesRequest request, AuthDAO authDao, GameDAO gameDao) throws UnauthorizedException, BadRequestException {
        try {
            String authToken = request.authToken();

            if (authToken == null) {
                throw new BadRequestException("ERROR: no Auth Token");
            }
            if (!authDao.containsAuthToken(authToken)) {
                throw new UnauthorizedException("ERROR: Auth Token invalid");
            }

            Collection<Game> games = gameDao.getListGames();
            return new ListGamesResult(games);
        } catch (NullPointerException e) {
            throw new UnauthorizedException("ERROR: no Auth Token");
        }
    }
}
