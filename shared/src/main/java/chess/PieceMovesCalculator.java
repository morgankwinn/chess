package chess;

import java.util.ArrayList;
import java.util.Collection;

public class PieceMovesCalculator {

    public static Collection<ChessMove> pieceMoveCalc(ChessBoard board, ChessPosition position) {
        ChessPiece piece = board.getPiece(position);
        ArrayList<ChessMove> moves = new ArrayList<>();

        if (piece.getPieceType() == ChessPiece.PieceType.BISHOP) {
            PieceMovesCalculatorBishop.bishopMoveCalc(board, position, moves);
        }
        // other piece types go here

        return moves;
    }
}
