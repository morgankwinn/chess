package dataaccess;

import model.Game;

import java.util.HashMap;

public class MemoryGameDAO implements GameDAO {
    private final HashMap<String, Game> games = new HashMap<>();
    
    @Override
    public void clearGames() {
        games.clear();
    }
}
