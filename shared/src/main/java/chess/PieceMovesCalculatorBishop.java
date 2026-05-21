package chess;

import java.util.ArrayList;

import static chess.PieceMovesCalculator.moveOneDirection;

public class PieceMovesCalculatorBishop {
    public static void bishopMoveCalc(ChessBoard board, ChessPosition position, ArrayList<ChessMove> moves) {
        // Up and to the left diagonal
        moveOneDirection(board, position, "UL", moves);
        // Up and to the right diagonal
        moveOneDirection(board, position, "UR", moves);
        // Down and to the left diagonal
        moveOneDirection(board, position, "DL", moves);
        // Down and to the right diagonal
        moveOneDirection(board, position, "DR", moves);
    }
}
