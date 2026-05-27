package dataaccess;

import chess.ChessGame;
import com.google.gson.Gson;
import model.Game;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;

public class MySQLGameDAO implements GameDAO {
    @Override
    public void clearGames() throws DataAccessException {
        String statement = "TRUNCATE TABLE game";
        DatabaseManager.executeUpdate(statement);
    }

    @Override
    public int addGame(String whiteUsername, String blackUsername, String gameName) throws DataAccessException {
        var statement = "INSERT INTO game (whiteUsername, blackUsername, gameName, game) VALUES (?, ?, ?, ?)";
        ChessGame game = new ChessGame();
        String gameString = new Gson().toJson(game.toString());
        int id = DatabaseManager.executeUpdate(statement, whiteUsername, blackUsername, gameName, gameString);

        if (id != 0) {
            return id;
        } else {
            throw new DataAccessException("ERROR: game not added");
        }
    }

    @Override
    public Game getGame(int gameID) throws DataAccessException {
        String statement = "SELECT * FROM game where gameID=?";
        try (Connection conn = DatabaseManager.getConnection()) {
            PreparedStatement ps = conn.prepareStatement(statement);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                String whiteUsername = rs.getNString("whiteUsername");
                String blackUsername = rs.getNString("blackUsername");
                String gameName = rs.getNString("gameName");
                String gameString = rs.getNString("game");
                ChessGame game = (ChessGame) new Gson().fromJson(gameString, ChessGame.class);

                return new Game(gameID, whiteUsername, blackUsername, gameName, game);
            }
            return null;
        } catch (SQLException e) {
            throw new DataAccessException("ERROR: Could not establish a connection to the database");
        }
    }

    @Override
    public Collection<Game> getListGames() throws DataAccessException {
        ArrayList<Game> gamesList = new ArrayList<>();
        try (Connection conn = DatabaseManager.getConnection()) {
            String statement = "SELECT * FROM game";
            PreparedStatement ps = conn.prepareStatement(statement);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                int gameID = rs.getInt("gameID");
                String whiteUsername = rs.getNString("whiteUsername");
                String blackUsername = rs.getNString("blackUsername");
                String gameName = rs.getNString("gameName");
                String gameString = rs.getNString("game");
                ChessGame game = (ChessGame) new Gson().fromJson(gameString, ChessGame.class);

                gamesList.add(new Game(gameID, whiteUsername, blackUsername, gameName, game));
            }
            return gamesList;
        } catch (SQLException e) {
            throw new DataAccessException("ERROR: Could not get games list");
        }
    }

    @Override
    public void addUserToGame(int gameID, String username, ChessGame.TeamColor playerColor) throws DataAccessException {
        String statement = "";

        if (playerColor == ChessGame.TeamColor.WHITE) {
            statement = "UPDATE game SET whiteUsername=? WHERE gameID=?";
        } else {
            statement = "UPDATE game SET blackUsername=? WHERE gameID=?";
        }
        
        DatabaseManager.executeUpdate(statement, username, gameID);
    }
}
