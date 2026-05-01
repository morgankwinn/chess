package chess;

import java.util.ArrayList;
import java.util.Collection;

public class PieceMovesCalculator {

    public static Collection<ChessMove> pieceMoveCalc(ChessBoard board, ChessPosition position) {
        ChessPiece piece = board.getPiece(position);
        Collection<ChessMove> moves = new ArrayList<>();

        if (piece.getPieceType() == ChessPiece.PieceType.BISHOP) {
            moves = PieceMovesCalculatorBishop.bishopMoveCalc(board, position);
        }

        return moves;
    }
}
