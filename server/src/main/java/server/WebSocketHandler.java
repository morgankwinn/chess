package server;

import chess.ChessGame;
import chess.ChessMove;
import chess.ChessPosition;
import chess.InvalidMoveException;
import com.google.gson.Gson;
import dataaccess.AuthDAO;
import dataaccess.DataAccessException;
import dataaccess.GameDAO;
import dataaccess.UserDAO;
import io.javalin.websocket.WsCloseContext;
import io.javalin.websocket.WsCloseHandler;
import io.javalin.websocket.WsConnectContext;
import io.javalin.websocket.WsConnectHandler;
import io.javalin.websocket.WsMessageContext;
import io.javalin.websocket.WsMessageHandler;
import model.Game;
import websocket.commands.MakeMoveCommand;
import websocket.commands.UserGameCommand;
import org.eclipse.jetty.websocket.api.Session;
import websocket.messages.ErrorMessage;
import websocket.messages.LoadGameMessage;
import websocket.messages.NotificationMessage;
import websocket.messages.ServerMessage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;

public class WebSocketHandler implements WsConnectHandler, WsMessageHandler, WsCloseHandler {
    private final ConnectionManager connections = new ConnectionManager();
    private final UserDAO userDao;
    private final AuthDAO authDao;
    private final GameDAO gameDao;

    public WebSocketHandler(UserDAO userDao, AuthDAO authDao, GameDAO gameDao) {
        this.userDao = userDao;
        this.authDao = authDao;
        this.gameDao = gameDao;
    }

    @Override
    public void handleConnect(WsConnectContext ctx) {
        System.out.println("Websocket connected");
        ctx.enableAutomaticPings();
    }

    @Override
    public void handleMessage(WsMessageContext ctx) {
        try {
            UserGameCommand command = new Gson().fromJson(ctx.message(), UserGameCommand.class);
            switch (command.getCommandType()) {
                case CONNECT -> connect(command, ctx.session);
                case MAKE_MOVE -> makeMove(new Gson().fromJson(ctx.message(), MakeMoveCommand.class), ctx.session);
                case LEAVE -> leave(command, ctx.session);
                case RESIGN -> resign(command, ctx.session);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void handleClose(WsCloseContext ctx) {
        System.out.println("Websocket closed");
    }

    private void connect(UserGameCommand command, Session session) throws IOException {
        connections.add(session);
        String username = getUsername(command, session);
        Game game = getGame(command, session);
        ChessGame.TeamColor playerColor = getPlayerColor(username, game);

        LoadGameMessage message = new LoadGameMessage(ServerMessage.ServerMessageType.LOAD_GAME, game);
        String messageJson = new Gson().toJson(message);
        if (session.isOpen()) {
            session.getRemote().sendString(messageJson);
        }

        String broadcast;
        if (playerColor == null) {
            broadcast = username + " has connected as an observer";
        } else {
            broadcast =
                    username + " has connected as " + playerColor.toString().toLowerCase();
        }
        NotificationMessage notificationMessage =
                new NotificationMessage(ServerMessage.ServerMessageType.NOTIFICATION, broadcast);
        connections.broadcast(session, notificationMessage);
    }

    private void makeMove(MakeMoveCommand command, Session session) throws IOException {
        ChessMove move = command.getMove();
        String username = getUsername(command, session);
        Game game = getGame(command, session);
        ChessGame.TeamColor playerColor = getPlayerColor(username, game);
        ChessGame chessGame = getGame(command, session).getGame();
        try {
            if (!isValidMove(chessGame, move)) {
                throw new Exception();
            } else if (playerColor != chessGame.getBoard().getPiece(move.getStartPosition()).getTeamColor()) {
                throw new Exception();
            } else if (!authDao.containsAuthToken(command.getAuthToken())) {
                throw new Exception();
            } else if (playerColor == null) {
                throw new Exception();
            } else if (Objects.equals(game.getState(), "gameover")) {
                throw new Exception();
            }
            chessGame.makeMove(move);
            gameDao.updateGame(command.getGameID(), chessGame);
        } catch (Exception e) {
            sendErrorMessage(session, "ERROR: Could not make move");
        }

        connections.broadcast(null,
                new LoadGameMessage(ServerMessage.ServerMessageType.LOAD_GAME, getGame(command, session)));
        String broadcast = move.getStartPosition().toString() + " has moved to " + move.getEndPosition().toString();
        connections.broadcast(session,
                new NotificationMessage(ServerMessage.ServerMessageType.NOTIFICATION, broadcast));

        if (chessGame.isInCheck(ChessGame.TeamColor.WHITE)) {
            connections.broadcast(null,
                    new NotificationMessage(ServerMessage.ServerMessageType.NOTIFICATION,
                            "white side is in check"));
        } else if (chessGame.isInCheck(ChessGame.TeamColor.BLACK)) {
            connections.broadcast(null,
                    new NotificationMessage(ServerMessage.ServerMessageType.NOTIFICATION,
                            "black side is in check"));
        } else if (chessGame.isInCheckmate(ChessGame.TeamColor.WHITE)) {
            connections.broadcast(null,
                    new NotificationMessage(ServerMessage.ServerMessageType.NOTIFICATION,
                            "white side is in checkmate"));
        } else if (chessGame.isInCheckmate(ChessGame.TeamColor.BLACK)) {
            connections.broadcast(null,
                    new NotificationMessage(ServerMessage.ServerMessageType.NOTIFICATION,
                            "black side is in checkmate"));
        } else if (chessGame.isInStalemate(ChessGame.TeamColor.WHITE)) {
            connections.broadcast(null,
                    new NotificationMessage(ServerMessage.ServerMessageType.NOTIFICATION,
                            "white side is in a stalemate"));
        } else if (chessGame.isInStalemate(ChessGame.TeamColor.BLACK)) {
            connections.broadcast(null,
                    new NotificationMessage(ServerMessage.ServerMessageType.NOTIFICATION,
                            "black side is in a stalemate"));
        }
    }

    private void leave(UserGameCommand command, Session session) throws IOException {
        String username = getUsername(command, session);
        Game game = getGame(command, session);
        ChessGame.TeamColor playerColor = getPlayerColor(username, game);
        try {
            gameDao.addUserToGame(command.getGameID(), null, playerColor);
        } catch (DataAccessException e) {
            sendErrorMessage(session, "ERROR: Could not leave game");
        }
        connections.remove(session);

        String broadcast;
        if (playerColor == null) {
            broadcast = username + " has left the game as an observer";
        } else {
            broadcast =
                    username + " has left the game as " + playerColor.toString().toLowerCase();
        }
        NotificationMessage notificationMessage =
                new NotificationMessage(ServerMessage.ServerMessageType.NOTIFICATION, broadcast);
        connections.broadcast(session, notificationMessage);
    }

    private void resign(UserGameCommand command, Session session) throws IOException {
        String username = getUsername(command, session);
        Game game = getGame(command, session);
        ChessGame.TeamColor playerColor = getPlayerColor(username, game);

        if (playerColor == null) {
            sendErrorMessage(session, "ERROR: Observers cannot resign");
        } else if (Objects.equals(game.getState(), "gameover")) {
            sendErrorMessage(session, "ERROR: Already resigned");
        }

        try {
            gameDao.updateState(command.getGameID(), "gameover");
        } catch (DataAccessException e) {
            throw new IOException("ERROR: Could not update game state");
        }

        String broadcast = username + " has resigned";
        NotificationMessage notificationMessage =
                new NotificationMessage(ServerMessage.ServerMessageType.NOTIFICATION, broadcast);
        connections.broadcast(null, notificationMessage);
    }

    private static ChessGame.TeamColor getPlayerColor(String username, Game game) {
        ChessGame.TeamColor playerColor;
        if (Objects.equals(username, game.whiteUsername())) {
            playerColor = ChessGame.TeamColor.WHITE;
        } else if (Objects.equals(username, game.blackUsername())) {
            playerColor = ChessGame.TeamColor.BLACK;
        } else {
            playerColor = null;
        }
        return playerColor;
    }

    private Game getGame(UserGameCommand command, Session session) throws IOException {
        Game game = null;
        try {
            game = gameDao.getGame(command.getGameID());
        } catch (Exception e) {
            sendErrorMessage(session, "ERROR: Invalid authToken");
        }
        validateGame(session, game);
        return game;
    }

    private void validateGame(Session session, Game game) throws IOException {
        if (game == null) {
            sendErrorMessage(session, "ERROR: Invalid gameID");
        }
    }

    private String getUsername(UserGameCommand command, Session session) throws IOException {
        String username = null;
        try {
            username = authDao.getUser(command.getAuthToken()).username();
        } catch (Exception e) {
            sendErrorMessage(session, "ERROR: Invalid authToken");
        }
        return username;
    }

    private boolean isValidMove(ChessGame chessGame, ChessMove move) {
        boolean isValid = false;
        Collection<ChessMove> validMoves = chessGame.validMoves(move.getStartPosition());
        for (ChessMove validMove : validMoves) {
            if (Objects.equals(move.toString(), validMove.toString())) {
                isValid = true;
                break;
            }
        }
        return isValid;
    }

    private void sendErrorMessage(Session session, String message) throws IOException {
        ErrorMessage errorMessage =
                new ErrorMessage(ServerMessage.ServerMessageType.ERROR, message);
        String errorMessageJson = new Gson().toJson(errorMessage);
        if (session.isOpen()) {
            session.getRemote().sendString(errorMessageJson);
        }
        throw new IOException(message);
    }

    private void sendNotificationMessage(Session session, String message) throws IOException {
        NotificationMessage notificationMessage =
                new NotificationMessage(ServerMessage.ServerMessageType.NOTIFICATION, message);
        String notificationMessageJson = new Gson().toJson(notificationMessage);
        if (session.isOpen()) {
            session.getRemote().sendString(notificationMessageJson);
        }
    }

    private void sendLoadGameMessage(Session session, Game game) throws IOException {
        LoadGameMessage loadGameMessage =
                new LoadGameMessage(ServerMessage.ServerMessageType.LOAD_GAME, game);
        String loadGameMessageJson = new Gson().toJson(loadGameMessage);
        if (session.isOpen()) {
            session.getRemote().sendString(loadGameMessageJson);
        }
    }
}
