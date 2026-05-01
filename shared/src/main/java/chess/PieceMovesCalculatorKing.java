package chess;

import java.util.ArrayList;

public class PieceMovesCalculatorKing {

    public static void kingMoveCalc(ChessBoard board, ChessPosition position, ArrayList<ChessMove> moves) {
        // Up and to the left direction
        moveOneDirection(board, position, moves, +1, -1);
        // get other directions...
    }

    private static void moveOneDirection(ChessBoard board, ChessPosition position, ArrayList<ChessMove> moves, int newRowAdd, int newColAdd) {
        ChessPiece piece = board.getPiece(position);
        ChessGame.TeamColor team = piece.getTeamColor();

        int newRow = position.getRow()+newRowAdd;
        int newCol = position.getColumn()+newColAdd;
        ChessPosition newPos = new ChessPosition(newRow, newCol);

        if (board.getPiece(newPos) != null) {
            ChessPiece new_piece = board.getPiece(newPos);
            if (new_piece.getTeamColor() == team) { return; }
        }
        moves.add(new ChessMove(position, newPos, null));
    }
}
