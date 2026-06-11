package websocket;

import chess.ChessMove;
import com.google.gson.Gson;
import jakarta.websocket.*;
import ui.GameplayClient;
import ui.PreLoginClient;
import websocket.commands.MakeMoveCommand;
import websocket.commands.UserGameCommand;
import websocket.messages.ErrorMessage;
import websocket.messages.LoadGameMessage;
import websocket.messages.NotificationMessage;
import websocket.messages.ServerMessage;

import java.io.IOException;
import java.net.URI;

public class WebSocketFacade extends Endpoint {
    Session session;

    public WebSocketFacade(String url) throws RuntimeException {
        try {
            url = url.replace("http", "ws");
            URI socketURI = new URI(url + "/ws");

            WebSocketContainer container = ContainerProvider.getWebSocketContainer();
            this.session = container.connectToServer(this, socketURI);

            //set message handler
            this.session.addMessageHandler(new MessageHandler.Whole<String>() {
                @Override
                public void onMessage(String message) {
                    ServerMessage serverMessage = new Gson().fromJson(message, ServerMessage.class);
                    if (serverMessage.getServerMessageType() == ServerMessage.ServerMessageType.ERROR) {
                        ErrorMessage errorMessage = new Gson().fromJson(message, ErrorMessage.class);
                        PreLoginClient.notify(errorMessage.getErrorMessage());
                    } else if (serverMessage.getServerMessageType() == ServerMessage.ServerMessageType.NOTIFICATION) {
                        NotificationMessage notification = new Gson().fromJson(message, NotificationMessage.class);
                        System.out.println(notification.getMessage());
                    } else if (serverMessage.getServerMessageType() == ServerMessage.ServerMessageType.LOAD_GAME) {
                        LoadGameMessage loadGameMessage = new Gson().fromJson(message, LoadGameMessage.class);
                        GameplayClient.game = loadGameMessage.getGame().getGame();
                        System.out.println();
                        GameplayClient.redrawBoard();
                        PreLoginClient.printPrompt();
                    }
                }
            });
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    //Endpoint requires this method, but you don't have to do anything
    @Override
    public void onOpen(Session session, EndpointConfig endpointConfig) {
    }

    public void connect(String authToken, int gameID) {
        try {
            UserGameCommand command = new UserGameCommand(UserGameCommand.CommandType.CONNECT, authToken, gameID);
            this.session.getBasicRemote().sendText(new Gson().toJson(command));
        } catch (IOException ex) {
            throw new RuntimeException("ERROR: Could not connect");
        }
    }

    public void makeMove(String authToken, int gameID, ChessMove move) {
        try {
            MakeMoveCommand command =
                    new MakeMoveCommand(UserGameCommand.CommandType.MAKE_MOVE, authToken, gameID, move);
            this.session.getBasicRemote().sendText(new Gson().toJson(command));
        } catch (IOException ex) {
            throw new RuntimeException("ERROR: Could not make move");
        }
    }

    public void leave(String authToken, int gameID) {
        try {
            UserGameCommand command = new UserGameCommand(UserGameCommand.CommandType.LEAVE, authToken, gameID);
            this.session.getBasicRemote().sendText(new Gson().toJson(command));
        } catch (IOException ex) {
            throw new RuntimeException("ERROR: Could not leave");
        }
    }

    public void resign(String authToken, int gameID) {
        try {
            UserGameCommand command = new UserGameCommand(UserGameCommand.CommandType.RESIGN, authToken, gameID);
            this.session.getBasicRemote().sendText(new Gson().toJson(command));
        } catch (IOException ex) {
            throw new RuntimeException("ERROR: Could not leave");
        }
    }
}
