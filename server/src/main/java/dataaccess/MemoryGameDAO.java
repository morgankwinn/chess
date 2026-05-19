package dataaccess;

import model.Game;

import java.util.Collection;
import java.util.HashMap;

public class MemoryGameDAO implements GameDAO {
    private final HashMap<String, Game> games = new HashMap<>();

    @Override
    public void clearGames() {
        games.clear();
    }

    @Override
    public void addGame(Game game) {
        games.put(game.gameName(), game);
    }

    @Override
    public Collection<Game> getListGames() {
        return games.values();
    }
}
