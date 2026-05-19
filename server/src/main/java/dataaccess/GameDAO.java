package dataaccess;

import chess.ChessGame;
import model.Game;

import java.util.Collection;

public interface GameDAO {
    void clearGames();

    int addGame(String whiteUsername, String blackUsername, String gameName);

    Game getGame(int gameID);

    Collection<Game> getListGames();

    void addUserToGame(int gameID, String username, ChessGame.TeamColor playerColor);
}
