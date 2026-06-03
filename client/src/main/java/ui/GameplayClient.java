package ui;

import chess.ChessBoard;
import chess.ChessGame;
import chess.ChessPiece;
import chess.ChessPosition;

public class GameplayClient {
    private static ChessBoard board;

    enum Color {
        white,
        black
    }

    // Uses a new chess game
    public void run() {
        ChessGame game = new ChessGame();
        board = game.getBoard();

        if (LoginClient.playerColor == ChessGame.TeamColor.BLACK) {
            drawBoardBlackSide();
        }
        drawBoardWhiteSide();
    }

    private static void drawBoardWhiteSide() {
        Color initColor = Color.white;

        drawWhiteHeaders();

        for (int i = 8; i >= 1; i--) {
            drawRow(i, initColor);
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

        for (int i = 8; i >= 1; i--) {
            drawRow(i, initColor);
            if (initColor == Color.white) {
                initColor = Color.black;
            } else {
                initColor = Color.white;
            }
        }

        drawBlackHeaders();
    }

    private static void drawRow(int i, Color initColor) {
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
                        " " + i + " ");
    }

    private static void drawSquare(int i, int j, Color squareColor) {
        ChessPiece piece = board.getPiece(new ChessPosition(i, j));
        String pieceSymbol = getPieceSymbol(piece.getPieceType());
        String esPieceColor = null;
        if (piece.getTeamColor() == ChessGame.TeamColor.WHITE) {
            esPieceColor = EscapeSequences.SET_TEXT_COLOR_BLUE;
        } else {
            esPieceColor = EscapeSequences.SET_TEXT_COLOR_RED;
        }

        String esSquareColor = null;
        if (squareColor == Color.white) {
            esSquareColor = EscapeSequences.SET_BG_COLOR_WHITE;
        } else {
            esSquareColor = EscapeSequences.SET_BG_COLOR_BLACK;
        }

        System.out.print(esSquareColor + esPieceColor + " " + pieceSymbol + " ");
    }

    private static void drawWhiteHeaders() {
        System.out.println(
                EscapeSequences.SET_BG_COLOR_LIGHT_GREY +
                        EscapeSequences.SET_TEXT_COLOR_BLACK +
                        "    a  b  c  d  e  f  g  h    ");
    }

    private static void drawBlackHeaders() {
        System.out.println(
                EscapeSequences.SET_BG_COLOR_LIGHT_GREY +
                        EscapeSequences.SET_TEXT_COLOR_BLACK +
                        "    h  g  f  e  d  c  b  a    ");
    }

    private static String getPieceSymbol(ChessPiece.PieceType pieceType) {
        return switch (pieceType) {
            case KING -> "K";
            case QUEEN -> "Q";
            case BISHOP -> "B";
            case KNIGHT -> "N";
            case ROOK -> "R";
            case PAWN -> "P";
        };
    }
}
