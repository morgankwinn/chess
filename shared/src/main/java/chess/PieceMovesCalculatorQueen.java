package chess;

import java.util.ArrayList;

public class PieceMovesCalculatorQueen {

    private static int newRow;
    private static int newCol;

    public static void queenMoveCalc(ChessBoard board, ChessPosition position, ArrayList<ChessMove> moves) {
        // Up and to the left direction
        moveOneDirection(board, position, moves, "UL");
        // Up direction
        moveOneDirection(board, position, moves, "U");
        // Up and to the right direction
        moveOneDirection(board, position, moves, "UR");
        // Right direction
        moveOneDirection(board, position, moves, "R");
        // Down and to the right direction
        moveOneDirection(board, position, moves, "DR");
        // Down direction
        moveOneDirection(board, position, moves, "D");
        // Down and to the left direction
        moveOneDirection(board, position, moves, "DL");
        // Left direction
        moveOneDirection(board, position, moves, "L");
    }

    private static void moveOneDirection(ChessBoard board, ChessPosition position, ArrayList<ChessMove> moves, String direction) {
        ChessPiece piece = board.getPiece(position);
        ChessGame.TeamColor team = piece.getTeamColor();
        int row = position.getRow();
        int col = position.getColumn();

        int i=1;
        calcNewPos(row, col, i, direction);
        while(newRow >= 1 && newRow <= 8 && newCol >= 1 && newCol <= 8) {
            ChessPosition newPos = new ChessPosition(newRow, newCol);

            if (board.getPiece(newPos) != null) {
                ChessPiece new_piece = board.getPiece(newPos);
                if (new_piece.getTeamColor() != team) {
                    moves.add(new ChessMove(position, newPos, null));
                }
                break;
            }
            moves.add(new ChessMove(position, newPos, null));
            i++;
            calcNewPos(row, col, i, direction);
        }
    }

    private static void calcNewPos(int row, int col, int i, String direction) {
        switch (direction) {
            case "UL" -> {
                newRow = row + i;
                newCol = col - i;
            }
            case "U" -> {
                newRow = row + i;
                newCol = col;
            }
            case "UR" -> {
                newRow = row + i;
                newCol = col + i;
            }
            case "R" -> {
                newRow = row;
                newCol = col + i;
            }
            case "DR" -> {
                newRow = row - i;
                newCol = col + i;
            }
            case "D" -> {
                newRow = row - i;
                newCol = col;
            }
            case "DL" -> {
                newRow = row - i;
                newCol = col - i;
            }
            default -> {
                newRow = row;
                newCol = col - i;
            }
        }
    }
}
