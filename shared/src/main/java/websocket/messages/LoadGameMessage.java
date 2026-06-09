package websocket.messages;

import model.Game;

public class LoadGameMessage extends ServerMessage {
    private final Game game;

    public LoadGameMessage(ServerMessageType type, Game game) {
        super(type);
        this.game = game;
    }

    public Game getGame() {
        return game;
    }
}
