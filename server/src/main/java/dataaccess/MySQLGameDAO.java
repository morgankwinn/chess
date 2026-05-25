package dataaccess;

import chess.ChessGame;
import model.Game;

import java.util.Collection;
import java.util.List;

public class MySQLGameDAO implements GameDAO{
    @Override
    public void clearGames() {

    }

    @Override
    public int addGame(String whiteUsername, String blackUsername, String gameName) {
        return 0;
    }

    @Override
    public Game getGame(int gameID) {
        return null;
    }

    @Override
    public Collection<Game> getListGames() {
        return List.of();
    }

    @Override
    public void addUserToGame(int gameID, String username, ChessGame.TeamColor playerColor) {

    }
}
