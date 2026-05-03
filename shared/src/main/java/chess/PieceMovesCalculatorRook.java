package chess;

import java.util.ArrayList;

public class PieceMovesCalculatorRook {

    private static int newRow;
    private static int newCol;

    public static void rookMoveCalc(ChessBoard board, ChessPosition position, ArrayList<ChessMove> moves) {
        // Up direction
        moveOneDirection(board, position, moves, "U");
        // Right direction
        moveOneDirection(board, position, moves, "R");
        // Down direction
        moveOneDirection(board, position, moves, "D");
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
            case "U" -> {
                newRow = row + i;
                newCol = col;
            }
            case "R" -> {
                newRow = row;
                newCol = col + i;
            }
            case "D" -> {
                newRow = row - i;
                newCol = col;
            }
            default -> {
                newRow = row;
                newCol = col - i;
            }
        }
    }
}
