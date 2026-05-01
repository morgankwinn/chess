package chess;

import java.util.ArrayList;

public class PieceMovesCalculatorBishop {

    private static int newRow;
    private static int newCol;

    public static void bishopMoveCalc(ChessBoard board, ChessPosition position, ArrayList<ChessMove> moves) {
        // Up and to the left diagonal
        calcMovesOneDiagonal(board, position, moves, "UL");
        // Up and to the right diagonal
        calcMovesOneDiagonal(board, position, moves, "UR");
        // Down and to the left diagonal
        calcMovesOneDiagonal(board, position, moves, "DL");
        // Down and to the right diagonal
        calcMovesOneDiagonal(board, position, moves, "DR");
    }

    private static void calcMovesOneDiagonal(ChessBoard board, ChessPosition position, ArrayList<ChessMove> moves, String direction) {
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
            case "UR" -> {
                newRow = row + i;
                newCol = col + i;
            }
            case "DL" -> {
                newRow = row - i;
                newCol = col - i;
            }
            default -> {
                newRow = row - i;
                newCol = col + i;
            }
        }
    }
}
