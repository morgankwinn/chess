package ui;

import chess.*;
import model.Game;
import model.result.ListGamesResult;
import websocket.WebSocketFacade;

import java.util.Objects;
import java.util.Scanner;

public class GameplayClient {
    private static ChessBoard board;
    private WebSocketFacade ws;

    enum Color {
        white,
        black
    }

    public void run() {
        ws = new WebSocketFacade("http://localhost:8080");
        ws.connect(PreLoginClient.authToken, LoginClient.gameID);
        ChessGame game = getGame();
        board = game.getBoard();

        System.out.print(help());

        Scanner scanner = new Scanner(System.in);
        String result = "";
        while (!result.equals("leave")) {
            PreLoginClient.printPrompt();
            String line = scanner.nextLine();

            try {
                result = eval(line);
                System.out.println(EscapeSequences.SET_TEXT_COLOR_BLUE + result);
            } catch (Exception e) {
                PreLoginClient.notify(e.getMessage());
            }
        }
    }

    private String eval(String input) throws RuntimeException {
        try {
            return switch (input.toLowerCase()) {
                case "makemove" -> makeMove();
                case "highlightlegalmoves" -> highlightMoves();
                case "redrawchessboard" -> redrawBoard();
                case "resign" -> resign();
                case "leave" -> leave();
                default -> help();
            };
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    private String makeMove() {
        Scanner scanner = new Scanner(System.in);

        try {
            System.out.println("Where are you moving from?");
            PreLoginClient.printPrompt();
            String startString = scanner.nextLine();
            ChessPosition start = interpretPosition(startString);

            System.out.println("Where are you moving to?");
            PreLoginClient.printPrompt();
            String endString = scanner.nextLine();
            ChessPosition end = interpretPosition(endString);

            ChessPiece.PieceType promo = null;
            if (board.getPiece(start).getPieceType() == ChessPiece.PieceType.PAWN) {
                if ((board.getPiece(start).getTeamColor() == ChessGame.TeamColor.BLACK && end.getRow() == 1) ||
                        (board.getPiece(start).getTeamColor() == ChessGame.TeamColor.WHITE && end.getRow() == 8)) {
                    System.out.println("Where is your piece being promoted to?");
                    PreLoginClient.printPrompt();
                    String promoString = scanner.nextLine();
                    promo = interpretPromotion(promoString);
                }
            }

            ChessMove move = new ChessMove(start, end, promo);
            ws.makeMove(PreLoginClient.authToken, LoginClient.gameID, move);
            return "Move successful";
        } catch (Exception e) {
            throw new RuntimeException("ERROR: Could not make move");
        }
    }

    //    Allows the user to input the piece for which they want to highlight legal moves.
//    The selected piece’s current square and all squares it can legally move to are highlighted.
//    This is a local operation and has no effect on remote users’ screens.
    private String highlightMoves() {
        return "";
    }

    private String redrawBoard() {
        if (LoginClient.playerColor == ChessGame.TeamColor.BLACK) {
            drawBoardBlackSide();
        } else {
            drawBoardWhiteSide();
        }
        return "Board successfully redrawn";
    }

    private String resign() throws RuntimeException {
        Scanner scanner = new Scanner(System.in);

        try {
            System.out.println("Are you sure you want to resign? (y/n)");
            PreLoginClient.printPrompt();
            String response = scanner.nextLine();
            if (response.toLowerCase().equals("y")) {
                ws.resign(PreLoginClient.authToken, LoginClient.gameID);
                return "You have resigned successfully";
            } else {
                return "You have not resigned";
            }
        } catch (Exception e) {
            throw new RuntimeException("ERROR: Could not resign game");
        }
    }

    private String leave() {
        ws.leave(PreLoginClient.authToken, LoginClient.gameID);
        return "leave";
    }

    private String help() {
        return """
                 - makeMove
                 - highlightLegalMoves
                 - redrawChessBoard
                 - resign
                 - leave
                 - help
                """;
    }

    private static void drawBoardWhiteSide() {
        Color initColor = Color.white;
        drawWhiteHeaders();
        for (int i = 8; i >= 1; i--) {
            drawWhiteRow(i, initColor);
            if (initColor == Color.white) {
                initColor = Color.black;
            } else {
                initColor = Color.white;
            }
        }
        drawWhiteHeaders();
    }

    private static void drawBoardBlackSide() {
        Color initColor = Color.white;
        drawBlackHeaders();
        for (int i = 1; i <= 8; i++) {
            drawBlackRow(i, initColor);
            if (initColor == Color.white) {
                initColor = Color.black;
            } else {
                initColor = Color.white;
            }
        }
        drawBlackHeaders();
    }

    private static void drawWhiteRow(int i, Color initColor) {
        Color squareColor = initColor;
        System.out.print(
                EscapeSequences.SET_BG_COLOR_LIGHT_GREY +
                        EscapeSequences.SET_TEXT_COLOR_BLACK +
                        " " + i + " ");
        for (int j = 1; j <= 8; j++) {
            drawSquare(i, j, squareColor);
            if (squareColor == Color.white) {
                squareColor = Color.black;
            } else {
                squareColor = Color.white;
            }
        }
        System.out.print(
                EscapeSequences.SET_BG_COLOR_LIGHT_GREY +
                        EscapeSequences.SET_TEXT_COLOR_BLACK +
                        " " + i + " " + EscapeSequences.RESET_BG_COLOR + "\n");
    }

    private static void drawBlackRow(int i, Color initColor) {
        Color squareColor = initColor;
        System.out.print(
                EscapeSequences.SET_BG_COLOR_LIGHT_GREY +
                        EscapeSequences.SET_TEXT_COLOR_BLACK +
                        " " + i + " ");
        for (int j = 8; j >= 1; j--) {
            drawSquare(i, j, squareColor);
            if (squareColor == Color.white) {
                squareColor = Color.black;
            } else {
                squareColor = Color.white;
            }
        }
        System.out.print(
                EscapeSequences.SET_BG_COLOR_LIGHT_GREY +
                        EscapeSequences.SET_TEXT_COLOR_BLACK +
                        " " + i + " " + EscapeSequences.RESET_BG_COLOR + "\n");
    }

    private static void drawSquare(int i, int j, Color squareColor) {
        ChessPiece piece = board.getPiece(new ChessPosition(i, j));

        ChessPiece.PieceType type = null;
        if (piece != null) {
            type = piece.getPieceType();
        }

        String esSquareColor;
        if (squareColor == Color.white) {
            esSquareColor = EscapeSequences.SET_BG_COLOR_WHITE;
        } else {
            esSquareColor = EscapeSequences.SET_BG_COLOR_BLACK;
        }

        String esPieceColor;
        if (piece == null) {
            esPieceColor = esSquareColor;
        } else if (piece.getTeamColor() == ChessGame.TeamColor.WHITE) {
            esPieceColor = EscapeSequences.SET_TEXT_COLOR_BLUE;
        } else {
            esPieceColor = EscapeSequences.SET_TEXT_COLOR_RED;
        }

        String pieceSymbol = getPieceSymbol(type);

        System.out.print(esSquareColor + esPieceColor + " " + pieceSymbol + " ");
    }

    private static void drawWhiteHeaders() {
        System.out.println(
                EscapeSequences.SET_BG_COLOR_LIGHT_GREY +
                        EscapeSequences.SET_TEXT_COLOR_BLACK +
                        "    a  b  c  d  e  f  g  h    " +
                        EscapeSequences.RESET_BG_COLOR);
    }

    private static void drawBlackHeaders() {
        System.out.println(
                EscapeSequences.SET_BG_COLOR_LIGHT_GREY +
                        EscapeSequences.SET_TEXT_COLOR_BLACK +
                        "    h  g  f  e  d  c  b  a    " +
                        EscapeSequences.RESET_BG_COLOR);
    }

    private static String getPieceSymbol(ChessPiece.PieceType pieceType) {
        return switch (pieceType) {
            case KING -> "K";
            case QUEEN -> "Q";
            case BISHOP -> "B";
            case KNIGHT -> "N";
            case ROOK -> "R";
            case PAWN -> "P";
            case null -> " ";
        };
    }

    private ChessGame getGame() {
        ListGamesResult listGamesResult = PreLoginClient.server.listGames(PreLoginClient.authToken);
        ChessGame chessGame = null;
        int i = 1;
        for (Game game : listGamesResult.games()) {
            if (Objects.equals(LoginClient.gameID, i)) {
                chessGame = game.getGame();
                break;
            }
            i++;
        }
        return chessGame;
    }

    private ChessPosition interpretPosition(String position) throws RuntimeException {
        try {
            String colString = position.substring(1, 2).toLowerCase();
            int col;
            String rowString = position.substring(0, 1);
            int row;

            switch (colString) {
                case "a" -> col = 1;
                case "b" -> col = 2;
                case "c" -> col = 3;
                case "d" -> col = 4;
                case "e" -> col = 5;
                case "f" -> col = 6;
                case "g" -> col = 7;
                case "h" -> col = 8;
                default -> col = 0;
            }
            switch (rowString) {
                case "1" -> row = 1;
                case "2" -> row = 2;
                case "3" -> row = 3;
                case "4" -> row = 4;
                case "5" -> row = 5;
                case "6" -> row = 6;
                case "7" -> row = 7;
                case "8" -> row = 8;
                default -> row = 0;
            }

            return new ChessPosition(row, col);
        } catch (Exception e) {
            throw new RuntimeException("ERROR: Could not interpret position");
        }
    }

    private ChessPiece.PieceType interpretPromotion(String promoString) throws RuntimeException {
        try {
            switch (promoString.toLowerCase()) {
                case "k" -> {
                    return ChessPiece.PieceType.KING;
                }
                case "q" -> {
                    return ChessPiece.PieceType.QUEEN;
                }
                case "b" -> {
                    return ChessPiece.PieceType.BISHOP;
                }
                case "r" -> {
                    return ChessPiece.PieceType.ROOK;
                }
                case "n" -> {
                    return ChessPiece.PieceType.KNIGHT;
                }
                case "p" -> {
                    return ChessPiece.PieceType.PAWN;
                }
                default -> {
                    return null;
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("ERROR: Could not interpret promotion");
        }
    }
}
