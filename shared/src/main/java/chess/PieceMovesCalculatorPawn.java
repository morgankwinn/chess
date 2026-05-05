package chess;

import java.util.ArrayList;

public class PieceMovesCalculatorPawn {

    private static int newRow;
    private static int newCol;

    public static void pawnMoveCalc(ChessBoard board, ChessPosition position, ArrayList<ChessMove> moves) {
        moveForward(board, position, moves);
    }

    private static void moveForward(ChessBoard board, ChessPosition position, ArrayList<ChessMove> moves) {
        ChessPiece piece = board.getPiece(position);
        ChessGame.TeamColor team = piece.getTeamColor();
        int row = position.getRow();
        int col = position.getColumn();

        calcNewPos(row, col, "normal", team);
        if(newRow >= 1 && newRow <= 8 && newCol >= 1 && newCol <= 8) {
            ChessPosition newPos = new ChessPosition(newRow, newCol);
            if (board.getPiece(newPos) == null) {
                if ((team == ChessGame.TeamColor.WHITE && newRow == 8) || (team == ChessGame.TeamColor.BLACK && newRow == 1)) {
                    moves.add(new ChessMove(position, newPos, ChessPiece.PieceType.QUEEN));
                    moves.add(new ChessMove(position, newPos, ChessPiece.PieceType.BISHOP));
                    moves.add(new ChessMove(position, newPos, ChessPiece.PieceType.KNIGHT));
                    moves.add(new ChessMove(position, newPos, ChessPiece.PieceType.ROOK));
                }
                else {
                    moves.add(new ChessMove(position, newPos, null));
                }

                if ((team == ChessGame.TeamColor.WHITE && row == 2) || (team == ChessGame.TeamColor.BLACK && row == 7)) {
                    calcNewPos(row, col, "start", team);
                    newPos = new ChessPosition(newRow, newCol);
                    if (board.getPiece(newPos) == null) {
                        moves.add(new ChessMove(position, newPos, null));
                    }
                }
            }
        }

        calcNewPos(row, col, "captureL", team);
        if(newRow >= 1 && newRow <= 8 && newCol >= 1 && newCol <= 8) {
            ChessPosition newPos = new ChessPosition(newRow, newCol);
            if (board.getPiece(newPos) != null) {
                ChessPiece newPiece = board.getPiece(newPos);
                if (newPiece.getTeamColor() != team) {
                    if ((team == ChessGame.TeamColor.WHITE && newRow == 8) || (team == ChessGame.TeamColor.BLACK && newRow == 1)) {
                        moves.add(new ChessMove(position, newPos, ChessPiece.PieceType.QUEEN));
                        moves.add(new ChessMove(position, newPos, ChessPiece.PieceType.BISHOP));
                        moves.add(new ChessMove(position, newPos, ChessPiece.PieceType.KNIGHT));
                        moves.add(new ChessMove(position, newPos, ChessPiece.PieceType.ROOK));
                    }
                    else {
                        moves.add(new ChessMove(position, newPos, null));
                    }
                }
            }
        }

        calcNewPos(row, col, "captureR", team);
        if(newRow >= 1 && newRow <= 8 && newCol >= 1 && newCol <= 8) {
            ChessPosition newPos = new ChessPosition(newRow, newCol);
            if (board.getPiece(newPos) != null) {
                ChessPiece newPiece = board.getPiece(newPos);
                if (newPiece.getTeamColor() != team) {
                    if ((team == ChessGame.TeamColor.WHITE && newRow == 8) || (team == ChessGame.TeamColor.BLACK && newRow == 1)) {
                        moves.add(new ChessMove(position, newPos, ChessPiece.PieceType.QUEEN));
                        moves.add(new ChessMove(position, newPos, ChessPiece.PieceType.BISHOP));
                        moves.add(new ChessMove(position, newPos, ChessPiece.PieceType.KNIGHT));
                        moves.add(new ChessMove(position, newPos, ChessPiece.PieceType.ROOK));
                    }
                    else {
                        moves.add(new ChessMove(position, newPos, null));
                    }
                }
            }
        }
    }

    private static void calcNewPos(int row, int col, String direction, ChessGame.TeamColor team) {
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
}
