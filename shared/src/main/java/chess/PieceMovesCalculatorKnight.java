package chess;

import java.util.ArrayList;

public class PieceMovesCalculatorKnight {

    private static int newRow;
    private static int newCol;

    public static void knightMoveCalc(ChessBoard board, ChessPosition position, ArrayList<ChessMove> moves) {
        // Up 2 and left 1
        moveOneL(board, position, moves, "UL");
        // Up 2 and right 1
        moveOneL(board, position, moves, "UR");
        // Right 2 and up 1
        moveOneL(board, position, moves, "RU");
        // Right 2 and down 1
        moveOneL(board, position, moves, "RD");
        // Down 2 and right 1
        moveOneL(board, position, moves, "DR");
        // Down 2 and left 1
        moveOneL(board, position, moves, "DL");
        // Left 2 and down 1
        moveOneL(board, position, moves, "LD");
        // Left 2 and up 1
        moveOneL(board, position, moves, "LU");
    }

    private static void moveOneL(ChessBoard board, ChessPosition position, ArrayList<ChessMove> moves, String direction) {
        ChessPiece piece = board.getPiece(position);
        ChessGame.TeamColor team = piece.getTeamColor();
        int row = position.getRow();
        int col = position.getColumn();

        calcNewPos(row, col, direction);
        if (newRow >= 1 && newRow <= 8 && newCol >= 1 && newCol <= 8) {
            ChessPosition newPos = new ChessPosition(newRow, newCol);

            if (board.getPiece(newPos) != null) {
                ChessPiece newPiece = board.getPiece(newPos);
                if (newPiece.getTeamColor() != team) {
                    moves.add(new ChessMove(position, newPos, null));
                }
            } else {
                moves.add(new ChessMove(position, newPos, null));
            }
        }
    }

    private static void calcNewPos(int row, int col, String direction) {
        switch (direction) {
            case "UL" -> {
                newRow = row + 2;
                newCol = col - 1;
            }
            case "UR" -> {
                newRow = row + 2;
                newCol = col + 1;
            }
            case "RU" -> {
                newRow = row + 1;
                newCol = col + 2;
            }
            case "RD" -> {
                newRow = row - 1;
                newCol = col + 2;
            }
            case "DR" -> {
                newRow = row - 2;
                newCol = col + 1;
            }
            case "DL" -> {
                newRow = row - 2;
                newCol = col - 1;
            }
            case "LD" -> {
                newRow = row - 1;
                newCol = col - 2;
            }
            default -> {
                newRow = row + 1;
                newCol = col - 2;
            }
        }
    }
}
