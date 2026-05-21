package chess;

import java.util.ArrayList;

import static chess.PieceMovesCalculator.moveOneDirection;

public class PieceMovesCalculatorQueen {
    public static void queenMoveCalc(ChessBoard board, ChessPosition position, ArrayList<ChessMove> moves) {
        // Up and to the left direction
        moveOneDirection(board, position, "UL", moves);
        // Up direction
        moveOneDirection(board, position, "U", moves);
        // Up and to the right direction
        moveOneDirection(board, position, "UR", moves);
        // Right direction
        moveOneDirection(board, position, "R", moves);
        // Down and to the right direction
        moveOneDirection(board, position, "DR", moves);
        // Down direction
        moveOneDirection(board, position, "D", moves);
        // Down and to the left direction
        moveOneDirection(board, position, "DL", moves);
        // Left direction
        moveOneDirection(board, position, "L", moves);
    }
}
