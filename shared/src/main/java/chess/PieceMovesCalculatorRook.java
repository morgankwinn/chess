package chess;

import java.util.ArrayList;

import static chess.PieceMovesCalculator.moveOneDirection;

public class PieceMovesCalculatorRook {
    public static void rookMoveCalc(ChessBoard board, ChessPosition position, ArrayList<ChessMove> moves) {
        // Up direction
        moveOneDirection(board, position, "U", moves);
        // Right direction
        moveOneDirection(board, position, "R", moves);
        // Down direction
        moveOneDirection(board, position, "D", moves);
        // Left direction
        moveOneDirection(board, position, "L", moves);
    }
}
