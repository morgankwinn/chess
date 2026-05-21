package chess;

import java.util.ArrayList;

public class PieceMovesCalculatorKing {

    public static void kingMoveCalc(ChessBoard board, ChessPosition position, ArrayList<ChessMove> moves) {
        // Up and to the left direction
        moveOneDirection(board, position, moves, 1, -1);
        // Up direction
        moveOneDirection(board, position, moves, 1, 0);
        // Up and to the right direction
        moveOneDirection(board, position, moves, 1, 1);
        // Right direction
        moveOneDirection(board, position, moves, 0, 1);
        // Down and to the right direction
        moveOneDirection(board, position, moves, -1, 1);
        // Down direction
        moveOneDirection(board, position, moves, -1, 0);
        // Down and to the left direction
        moveOneDirection(board, position, moves, -1, -1);
        // Left direction
        moveOneDirection(board, position, moves, 0, -1);
    }

    private static void moveOneDirection(ChessBoard board, ChessPosition position, ArrayList<ChessMove> moves, int newRowAdd, int newColAdd) {
        ChessPiece piece = board.getPiece(position);
        ChessGame.TeamColor team = piece.getTeamColor();

        int newRow = position.getRow() + newRowAdd;
        int newCol = position.getColumn() + newColAdd;

        if (newRow >= 1 && newRow <= 8 && newCol >= 1 && newCol <= 8) {
            ChessPosition newPos = new ChessPosition(newRow, newCol);

            if (board.getPiece(newPos) != null) {
                ChessPiece newPiece = board.getPiece(newPos);
                if (newPiece.getTeamColor() == team) {
                    return;
                }
            }
            moves.add(new ChessMove(position, newPos, null));
        }
    }
}
