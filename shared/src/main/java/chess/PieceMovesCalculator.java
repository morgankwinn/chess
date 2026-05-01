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
        else if (piece.getPieceType() == ChessPiece.PieceType.KING) {
            PieceMovesCalculatorKing.kingMoveCalc(board, position, moves);
        }
        else if (piece.getPieceType() == ChessPiece.PieceType.KNIGHT) {
            PieceMovesCalculatorKnight.knightMoveCalc(board, position, moves);
        }
        else if (piece.getPieceType() == ChessPiece.PieceType.PAWN) {
            PieceMovesCalculatorPawn.pawnMoveCalc(board, position, moves);
        }
        else if (piece.getPieceType() == ChessPiece.PieceType.QUEEN) {
            PieceMovesCalculatorQueen.queenMoveCalc(board, position, moves);
        }
        else {
            PieceMovesCalculatorRook.rookMoveCalc(board, position, moves);
        }

        return moves;
    }
}
