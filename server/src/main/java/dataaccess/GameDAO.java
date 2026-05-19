package dataaccess;

import model.Game;

import java.util.Collection;

public interface GameDAO {
    void clearGames();

    int addGame(String whiteUsername, String blackUsername, String gameName);

    Collection<Game> getListGames();
}
