package chess;

import java.util.ArrayList;

public class PieceMovesCalculatorPawn {
    private static ChessBoard board;
    private static ChessPosition position;
    private static ArrayList<ChessMove> moves;
    private static ChessPiece piece;
    private static ChessGame.TeamColor team;
    private static int row;
    private static int col;
    private static int newRow;
    private static int newCol;

    public static void pawnMoveCalc(ChessBoard givenBoard, ChessPosition givenPosition,
                                    ArrayList<ChessMove> givenMoves) {
        board = givenBoard;
        position = givenPosition;
        moves = givenMoves;
        piece = board.getPiece(position);
        team = piece.getTeamColor();
        row = position.getRow();
        col = position.getColumn();

        moveForward();
    }

    private static void moveForward() {
        calcNewPos("normal");
        if (inBounds()) {
            ChessPosition newPos = new ChessPosition(newRow, newCol);
            if (board.getPiece(newPos) == null) {
                checkAndPromote(newPos);
                doubleMove();
            }
        }

        capture("captureL");
        capture("captureR");
    }

    private static void doubleMove() {
        ChessPosition newPos;
        if ((team == ChessGame.TeamColor.WHITE && row == 2) || (team == ChessGame.TeamColor.BLACK && row == 7)) {
            calcNewPos("start");
            newPos = new ChessPosition(newRow, newCol);
            if (board.getPiece(newPos) == null) {
                moves.add(new ChessMove(position, newPos, null));
            }
        }
    }

    private static void capture(String direction) {
        calcNewPos(direction);
        if (inBounds()) {
            ChessPosition newPos = new ChessPosition(newRow, newCol);
            if (board.getPiece(newPos) != null) {
                ChessPiece newPiece = board.getPiece(newPos);
                if (newPiece.getTeamColor() != team) {
                    checkAndPromote(newPos);
                }
            }
        }
    }

    private static void checkAndPromote(ChessPosition newPos) {
        if ((team == ChessGame.TeamColor.WHITE && newRow == 8) || (team == ChessGame.TeamColor.BLACK && newRow == 1)) {
            moves.add(new ChessMove(position, newPos, ChessPiece.PieceType.QUEEN));
            moves.add(new ChessMove(position, newPos, ChessPiece.PieceType.BISHOP));
            moves.add(new ChessMove(position, newPos, ChessPiece.PieceType.KNIGHT));
            moves.add(new ChessMove(position, newPos, ChessPiece.PieceType.ROOK));
        } else {
            moves.add(new ChessMove(position, newPos, null));
        }
    }

    private static void calcNewPos(String direction) {
        if (team == ChessGame.TeamColor.WHITE) {
            switch (direction) {
                case "captureL" -> {
                    newRow = row + 1;
                    newCol = col - 1;
                }
                case "captureR" -> {
                    newRow = row + 1;
                    newCol = col + 1;
                }
                case "start" -> {
                    newRow = row + 2;
                    newCol = col;
                }
                default -> {
                    newRow = row + 1;
                    newCol = col;
                }
            }
        }
        if (team == ChessGame.TeamColor.BLACK) {
            switch (direction) {
                case "captureL" -> {
                    newRow = row - 1;
                    newCol = col - 1;
                }
                case "captureR" -> {
                    newRow = row - 1;
                    newCol = col + 1;
                }
                case "start" -> {
                    newRow = row - 2;
                    newCol = col;
                }
                default -> {
                    newRow = row - 1;
                    newCol = col;
                }
            }
        }
    }

    private static boolean inBounds() {
        return (newRow >= 1 && newRow <= 8 && newCol >= 1 && newCol <= 8);
    }
}
