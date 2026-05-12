package chess;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;

/**
 * A class that can manage a chess game, making moves on a board
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessGame {

    private ChessBoard board;
    private ChessGame.TeamColor turn;

    public ChessGame() {
        this.board = new ChessBoard();
        this.turn = TeamColor.WHITE;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ChessGame chessGame = (ChessGame) o;
        return Objects.equals(board, chessGame.board) && turn == chessGame.turn;
    }

    @Override
    public int hashCode() {
        return Objects.hash(board, turn);
    }

    /**
     * @return Which team's turn it is
     */
    public TeamColor getTeamTurn() {
        return turn;
    }

    /**
     * Sets which teams turn it is
     *
     * @param team the team whose turn it is
     */
    public void setTeamTurn(TeamColor team) {
        turn = team;
    }

    /**
     * Enum identifying the 2 possible teams in a chess game
     */
    public enum TeamColor {
        WHITE,
        BLACK
    }

    /**
     * Gets all valid moves for a piece at the given location
     *
     * @param startPosition the piece to get valid moves for
     * @return Set of valid moves for requested piece, or null if no piece at
     * startPosition
     */
    public Collection<ChessMove> validMoves(ChessPosition startPosition) {
        if (board.getPiece(startPosition) == null) {
            return null;
        }

        ChessPiece piece = board.getPiece(startPosition);
        ChessPiece.PieceType type = piece.getPieceType();
        TeamColor team = piece.getTeamColor();

        Collection<ChessMove> moves = piece.pieceMoves(board, startPosition);
        Collection<ChessMove> newMoves = new ArrayList<>();

        //make a copy of the board
        ChessBoard boardClone = board.clone();
        ChessBoard originalBoard = board;
        this.board = boardClone;

        //simulate each move
        for (ChessMove move : moves) {
            ChessPiece enemyPiece = board.getPiece(move.getEndPosition());
            makeMoveHelper(move);
            //if valid add to the new list
            if (!isInCheck(team)) {
                newMoves.add(move);
            }
            makeMoveHelper(new ChessMove(move.getEndPosition(), move.getStartPosition(), type));
            board.addPiece(move.getEndPosition(), enemyPiece);
        }
        //switch back to OG board
        this.board = originalBoard;
        return newMoves;
    }

    /**
     * Makes a move in the chess game
     *
     * @param move chess move to perform
     * @throws InvalidMoveException if move is invalid
     */
    public void makeMove(ChessMove move) throws InvalidMoveException {
        ChessPiece piece = board.getPiece(move.getStartPosition());
        if ((piece != null) && (piece.getTeamColor() != turn)) {
            throw new InvalidMoveException();
        }
        Collection<ChessMove> vMoves = validMoves(move.getStartPosition());
        if (vMoves == null) {
            throw new InvalidMoveException();
        }

        for (ChessMove vMove : vMoves) {
            if (vMove.equals(move)) {
                makeMoveHelper(move);

                //advance turn
                if (getTeamTurn() == TeamColor.WHITE) {
                    setTeamTurn(TeamColor.BLACK);
                }
                else {
                    setTeamTurn(TeamColor.WHITE);
                }
                return;
            }
        }
        throw new InvalidMoveException();
    }

    private void makeMoveHelper(ChessMove move) {
        ChessPosition start = move.getStartPosition();
        ChessPosition end = move.getEndPosition();
        ChessPiece.PieceType promotion = move.getPromotionPiece();

        board.addPiece(end, board.getPiece(start));
        if (promotion != null) {
            board.addPiece(end, new ChessPiece(turn, promotion));
        }
        board.addPiece(start, null);
    }

    /**
     * Determines if the given team is in check
     *
     * @param teamColor which team to check for check
     * @return True if the specified team is in check
     */
    public boolean isInCheck(TeamColor teamColor) {
        //check all opposite teams moves
        for (int i = 1; i <= 8; i++) {
            for (int j = 1; j <= 8; j++) {
                ChessPiece oppPiece = board.getPiece(new ChessPosition(i, j));
                if (oppPiece != null) {
                    if (oppPiece.getTeamColor() != teamColor){
                        Collection<ChessMove> moves = oppPiece.pieceMoves(board, new ChessPosition(i, j));
                        for (ChessMove move : moves) {
                            ChessPiece pieceInDanger = board.getPiece(move.getEndPosition());
                            if (pieceInDanger != null) {
                                if (pieceInDanger.getPieceType() == ChessPiece.PieceType.KING) {
                                    return true;
                                }
                            }
                        }
                    }
                }
            }
        }
        return false;
    }

    /**
     * Determines if the given team is in checkmate
     *
     * @param teamColor which team to check for checkmate
     * @return True if the specified team is in checkmate
     */
    public boolean isInCheckmate(TeamColor teamColor) {
        throw new RuntimeException("Not implemented");
    }

    /**
     * Determines if the given team is in stalemate, which here is defined as having
     * no valid moves while not in check.
     *
     * @param teamColor which team to check for stalemate
     * @return True if the specified team is in stalemate, otherwise false
     */
    public boolean isInStalemate(TeamColor teamColor) {
        throw new RuntimeException("Not implemented");
    }

    /**
     * Sets this game's chessboard to a given board
     *
     * @param board the new board to use
     */
    public void setBoard(ChessBoard board) {
        this.board = board;
    }

    /**
     * Gets the current chessboard
     *
     * @return the chessboard
     */
    public ChessBoard getBoard() {
        return board;
    }
}
