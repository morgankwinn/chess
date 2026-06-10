package dataaccess;

import chess.ChessGame;
import model.Game;

import java.util.Collection;

public interface GameDAO {
    void clearGames() throws DataAccessException;

    int addGame(String whiteUsername, String blackUsername, String gameName) throws DataAccessException;

    Game getGame(int gameID) throws DataAccessException;

    Collection<Game> getListGames() throws DataAccessException;

    void addUserToGame(int gameID, String username, ChessGame.TeamColor playerColor) throws DataAccessException;

    void updateState(int gameID, String state) throws DataAccessException;

    void updateGame(int gameID, ChessGame game) throws DataAccessException;
}
