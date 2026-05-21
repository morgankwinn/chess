package model;

import chess.ChessGame;

import java.util.Objects;

public final class Game {
    private final int gameID;
    private String whiteUsername;
    private String blackUsername;
    private final String gameName;
    private final ChessGame game;

    public Game(int gameID, String whiteUsername, String blackUsername, String gameName, ChessGame game) {
        this.gameID = gameID;
        this.whiteUsername = whiteUsername;
        this.blackUsername = blackUsername;
        this.gameName = gameName;
        this.game = game;
    }

    public int gameID() {
        return gameID;
    }

    public String whiteUsername() {
        return whiteUsername;
    }

    public void setWhiteUsername(String username) {
        whiteUsername = username;
    }

    public String blackUsername() {
        return blackUsername;
    }

    public void setBlackUsername(String username) {
        blackUsername = username;
    }

    public String gameName() {
        return gameName;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (obj == null || obj.getClass() != this.getClass()) {
            return false;
        }
        var that = (Game) obj;
        return this.gameID == that.gameID &&
                Objects.equals(this.whiteUsername, that.whiteUsername) &&
                Objects.equals(this.blackUsername, that.blackUsername) &&
                Objects.equals(this.gameName, that.gameName) &&
                Objects.equals(this.game, that.game);
    }

    @Override
    public int hashCode() {
        return Objects.hash(gameID, whiteUsername, blackUsername, gameName, game);
    }

    @Override
    public String toString() {
        return "Game[" +
                "gameID=" + gameID + ", " +
                "whiteUsername=" + whiteUsername + ", " +
                "blackUsername=" + blackUsername + ", " +
                "gameName=" + gameName + ", " +
                "game=" + game + ']';
    }
}
