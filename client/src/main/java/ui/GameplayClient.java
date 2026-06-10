package ui;

import chess.ChessBoard;
import chess.ChessGame;
import chess.ChessPiece;
import chess.ChessPosition;

import java.util.Scanner;

public class GameplayClient {
    private static ChessBoard board;

    enum Color {
        white,
        black
    }

    public void run() {
        //need to implement actual game
        ChessGame game = new ChessGame();
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

    //    Allow the user to input what move they want to make.
//    The board is updated to reflect the result of the move, and the board automatically updates
//    on all clients involved in the game.
    private String makeMove() {
        return "";
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

    //    Prompts the user to confirm they want to resign.
//    If they do, the user forfeits the game and the game is over.
//    Does not cause the user to leave the game.
    private String resign() {
        return "";
    }

    //    Removes the user from the game (whether they are playing or observing the game).
//    The client transitions back to the Post-Login UI.
    private String leave() {
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
}
