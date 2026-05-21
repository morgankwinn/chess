package chess;

import java.util.ArrayList;
import java.util.Collection;

public class PieceMovesCalculator {
    private static int newRow;
    private static int newCol;

    public static Collection<ChessMove> pieceMoveCalc(ChessBoard board, ChessPosition position) {
        ChessPiece piece = board.getPiece(position);
        ArrayList<ChessMove> moves = new ArrayList<>();

        if (piece.getPieceType() == ChessPiece.PieceType.BISHOP) {
            PieceMovesCalculatorBishop.bishopMoveCalc(board, position, moves);
        } else if (piece.getPieceType() == ChessPiece.PieceType.KING) {
            PieceMovesCalculatorKing.kingMoveCalc(board, position, moves);
        } else if (piece.getPieceType() == ChessPiece.PieceType.KNIGHT) {
            PieceMovesCalculatorKnight.knightMoveCalc(board, position, moves);
        } else if (piece.getPieceType() == ChessPiece.PieceType.PAWN) {
            PieceMovesCalculatorPawn.pawnMoveCalc(board, position, moves);
        } else if (piece.getPieceType() == ChessPiece.PieceType.QUEEN) {
            PieceMovesCalculatorQueen.queenMoveCalc(board, position, moves);
        } else {
            PieceMovesCalculatorRook.rookMoveCalc(board, position, moves);
        }

        return moves;
    }

    public static void moveOneDirection(ChessBoard board, ChessPosition position, String direction,
                                        ArrayList<ChessMove> moves) {
        ChessPiece piece = board.getPiece(position);
        ChessGame.TeamColor team = piece.getTeamColor();
        int row = position.getRow();
        int col = position.getColumn();

        int i = 1;
        calcNewPos(row, col, i, direction);
        while (newRow >= 1 && newRow <= 8 && newCol >= 1 && newCol <= 8) {
            ChessPosition newPos = new ChessPosition(newRow, newCol);

            if (board.getPiece(newPos) != null) {
                ChessPiece newPiece = board.getPiece(newPos);
                if (newPiece.getTeamColor() != team) {
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
            case "U" -> {
                newRow = row + i;
                newCol = col;
            }
            case "UR" -> {
                newRow = row + i;
                newCol = col + i;
            }
            case "R" -> {
                newRow = row;
                newCol = col + i;
            }
            case "DR" -> {
                newRow = row - i;
                newCol = col + i;
            }
            case "D" -> {
                newRow = row - i;
                newCol = col;
            }
            case "DL" -> {
                newRow = row - i;
                newCol = col - i;
            }
            default -> {
                newRow = row;
                newCol = col - i;
            }
        }
    }
}
