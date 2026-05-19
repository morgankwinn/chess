package dataaccess;

import chess.ChessGame;
import model.Game;

import java.util.Collection;
import java.util.HashMap;

public class MemoryGameDAO implements GameDAO {
    private final HashMap<String, Game> games = new HashMap<>();
    public int nextID;

    public MemoryGameDAO() {
        nextID = 1;
    }

    @Override
    public void clearGames() {
        games.clear();
    }

    @Override
    public int addGame(String whiteUsername, String blackUsername, String gameName) {
        Game newGame = new Game(nextID, whiteUsername, blackUsername, gameName, new ChessGame());
        games.put(String.valueOf(nextID), newGame);
        nextID++;
        return newGame.gameID();
    }

    @Override
    public Game getGame(int gameID) {
        return games.get(String.valueOf(gameID));
    }

    @Override
    public Collection<Game> getListGames() {
        return games.values();
    }

    @Override
    public void addUserToGame(int gameID, String username, ChessGame.TeamColor playerColor) {
        Game game = getGame(gameID);

        if (playerColor == ChessGame.TeamColor.WHITE) {
            game.setWhiteUsername(username);
        } else {
            game.setBlackUsername(username);
        }
    }
}
