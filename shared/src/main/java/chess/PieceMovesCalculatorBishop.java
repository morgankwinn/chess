package chess;

import java.util.ArrayList;
import java.util.Collection;

public class PieceMovesCalculatorBishop {

    public static Collection<ChessMove> bishopMoveCalc(ChessBoard board, ChessPosition position) {
        ChessPiece piece = board.getPiece(position);
        ChessGame.TeamColor team = piece.getTeamColor();
        int row = position.getRow();
        int col = position.getColumn();
        ArrayList<ChessMove> moves = new ArrayList<>();

        // Up and to the left
        for(int i = 1; row+i <= 8 && col-i >= 1; i++) {
            if (board.getPiece(new ChessPosition(row+i, col-i)) != null) {
                ChessPiece new_piece = board.getPiece(new ChessPosition(row+i, col-i));
                if (new_piece.getTeamColor() != team) {
                    moves.add(new ChessMove(position, new ChessPosition(row + i, col - i), null));
                }
                break;
            }
            moves.add(new ChessMove(position, new ChessPosition(row+i, col-i), null));
        }
        //while() {}
        //while() {}
        //while() {}

        return moves;
    }

    //private MoveNW() {}
}
