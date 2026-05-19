package dataaccess;

import model.Game;

import java.util.Collection;

public interface GameDAO {
    void clearGames();

    void addGame(Game game);

    Collection<Game> getListGames();
}
