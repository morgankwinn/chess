package model.request;

import chess.ChessGame;

import java.util.Objects;

public final class JoinGameRequest {
    private String authToken;
    private final ChessGame.TeamColor playerColor;
    private final int gameID;

    public JoinGameRequest(String authToken, ChessGame.TeamColor playerColor, int gameID) {
        this.authToken = authToken;
        this.playerColor = playerColor;
        this.gameID = gameID;
    }

    public String authToken() {
        return authToken;
    }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }

    public ChessGame.TeamColor playerColor() {
        return playerColor;
    }

    public int gameID() {
        return gameID;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (obj == null || obj.getClass() != this.getClass()) {
            return false;
        }
        var that = (JoinGameRequest) obj;
        return Objects.equals(this.authToken, that.authToken) &&
                Objects.equals(this.playerColor, that.playerColor) &&
                this.gameID == that.gameID;
    }

    @Override
    public int hashCode() {
        return Objects.hash(authToken, playerColor, gameID);
    }

    @Override
    public String toString() {
        return "JoinGameRequest[" +
                "authToken=" + authToken + ", " +
                "playerColor=" + playerColor + ", " +
                "gameID=" + gameID + ']';
    }
}
