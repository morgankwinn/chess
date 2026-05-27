package service;

import chess.ChessGame;
import dataaccess.AuthDAO;
import dataaccess.DataAccessException;
import dataaccess.GameDAO;
import model.Game;
import request.JoinGameRequest;

public class JoinGameService {
    public void joinGame(JoinGameRequest request, GameDAO gameDao, AuthDAO authDao)
            throws UnauthorizedException, AlreadyTakenException, BadRequestException, DataAccessException {
        try {
            String authToken = request.authToken();

            if (authToken == null) {
                throw new BadRequestException("ERROR: no Auth Token");
            } else if (request.playerColor() == null) {
                throw new BadRequestException("ERROR: no Player Color");
            } else if (!authDao.containsAuthToken(authToken)) {
                throw new UnauthorizedException("ERROR: Auth Token invalid");
            }

            Game game = gameDao.getGame(request.gameID());
            if (game == null) {
                throw new BadRequestException("ERROR: Game does not exist");
            } else if (request.playerColor() == ChessGame.TeamColor.WHITE && game.whiteUsername() != null) {
                throw new AlreadyTakenException("ERROR: Team Color already taken");
            } else if (request.playerColor() == ChessGame.TeamColor.BLACK && game.blackUsername() != null) {
                throw new AlreadyTakenException("ERROR: Team Color already taken");
            }

            String username = authDao.getUser(authToken).username();

            gameDao.addUserToGame(request.gameID(), username, request.playerColor());
        } catch (NullPointerException e) {
            throw new UnauthorizedException("ERROR: no Auth Token");
        }
    }
}
